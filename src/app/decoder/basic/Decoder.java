package app.decoder.basic;

import app.model.Device;
import app.model.DeviceTracking;
import app.tools.Console;
import app.tools.SenderAlert;
import app.tools.SenderCalculator;
import app.tools.Tools;
import app.tools.trackingDebugger.helpers.Debugger;
import app.tools.trackingDebugger.helpers.ErrCodeList;
import app.tools.vondors.CodecException;
import config.DeviceConfigs;
import config.ListDevices;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Decoder implements Runnable {

    private final Socket socket;
    private static ArrayList<ConnectionDetails> connectedDevices = new ArrayList<>();//removable--#log

    public Decoder(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        DeviceConfigs connectedDeviceConfig = ListDevices.getDeviceConfigs(socket.getLocalPort());
        ConnectionDetails conLog = new ConnectionDetails(connectedDeviceConfig);//removable--#log
        conLog.addLogMsg("#############################################################");
        conLog.addLogMsgWithDate("--------------------|| new  socket ||-----------------------");
        connectedDevices.add(conLog);//removable--#log
        conLog.addLogMsgWithMs("onStart: " + connectedDevices.size() + " devices online");
        conLog.addLogMsgWithMs("port:" + socket.getLocalPort());
        int nbr_loop_socket = 0;
        String imei = null;
        while (!socket.isClosed()) {
            nbr_loop_socket++;
            if (nbr_loop_socket % 10 == 0) {
                Console.println("nbr_loop_socket: " + nbr_loop_socket + " - imei:" + imei + " - ip :" + socket.getRemoteSocketAddress());
            }
            if (connectedDeviceConfig == null)
                conLog.addLogMsgWithMs("Err: DeviceConfig not fond for this port");
            else {
                conLog.addLogMsgWithMs("DeviceConfig: " + connectedDeviceConfig.name);
                try {
                    DecoderDevice decoder = connectedDeviceConfig.getDecoder();
                    decoder.setSocket(socket);
                    decoder.initDataInputStream();
                    imei = decoder.getImei();
                    conLog.addLogMsgWithMs("imei: " + imei);
                    conLog.imei = imei;//removable--#log
                    if (imeiIsValid(imei)) {
                        Device device = Tools.deviceDAO.getDevice(imei);
                        if (device == null) {
                            decoder.closeDataIOStream();
                            conLog.addLogMsgWithMs("Err: imei not fond on DB: " + imei);
                        } else {
                            conLog.addLogMsgWithMs("device_id: " + device.getId());
                            decoder.handshake();
                            decoder.setDevice(device);
                            List<DeviceTracking> deviceTrackings = decoder.decode();
                            conLog.addLogMsgWithMs("trackings received: " + deviceTrackings.size());
                            Collections.reverse(deviceTrackings); //sort trackings by ( dateTracking asc)
                            ErrCodeList.ErrList errList = new ErrCodeList.ErrList();
                            conLog.addLogMsgWithMs("------start checkDeviceDataRealityAndInsert-------");
                            for (DeviceTracking deviceTracking : deviceTrackings) {
//                            conLog.addLogMsgWithMs("start checkDeviceDataRealityAndInsert");
                                checkDeviceDataRealityAndInsert(deviceTracking);
//                            conLog.addLogMsgWithMs("end checkDeviceDataRealityAndInsert");
                                if (deviceTracking.errCodes.size() > 0)
                                    errList.addErr(deviceTracking.errCodes);
                                else
                                    errList.noErr++;

                            }
                            conLog.addLogMsgWithMs("------end checkDeviceDataRealityAndInsert-------");

                            conLog.addLogMsgWithMs(String.format("=> %d/%d trackings inserted . ", errList.noErr, deviceTrackings.size()));
                            if (errList.hasErr())
                                conLog.addLogMsgWithMs(errList.toString());
                            decoder.feedBackOk(deviceTrackings.size());
                            decoder.closeIOStream();
                            Thread.currentThread().interrupt();
                        }
                    } else {
                        conLog.addLogMsgWithMs("Err: imei [" + imei + "] is invalid");
                    }

                } catch (IOException | CodecException e) {
                    conLog.addLogMsgWithMs("Err: IOException | CodecException => " + e.getMessage());
                    Console.printStackTrace(e);
                }
            }
            connectedDevices.remove(conLog);
            conLog.addLogMsgWithMs("connected devices onEnd: " + connectedDevices.size() + " devices online");
//        socket.close();
        }
        conLog.addLogMsgWithDate("--------------------|| End socket  ||-----------------------");
        conLog.addLogMsg("#############################################################");
        conLog.printLog();
    }

    private Boolean imeiIsValid(String imei) {
        return Tools.isNumeric(imei);
    }

    private void checkDeviceDataRealityAndInsert(DeviceTracking tracking) {
        List<Debugger> debuggers = tracking.deviceDetails.Debuggers;
        int debugger_index = 0;
        while (debugger_index < debuggers.size()) {
            Debugger debugger = debuggers.get(debugger_index);
            tracking = debugger.debugging(tracking);
            if (tracking.errCodes.size() > 0)
                return;
            debugger_index++;
        }
        Date tracking_date = Tools.DateTime.stringToDate(tracking.getTracking_time());
//        Console.printlnWithDate(tracking.getTracking_time() + "," + tracking.avlData);
        long diffTime = (new Date()).getTime() - tracking_date.getTime();
        if (Tools.trackingDAO.insertTracking(tracking, true, diffTime)) {
            SenderAlert.checkAndHandler(diffTime, tracking);
            SenderCalculator.checkAndHandler(tracking_date, diffTime, tracking);
        } else {
            tracking.errCodes.add(ErrCodeList.SQLErr);
        }
    }
}

package app.decoder.devices;

import app.decoder.basic.DecoderDevice;
import app.model.DeviceTracking;
import app.tools.Console;
import app.tools.Tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeiTrackDecoder extends DecoderDevice {

    private String[] data;

    public void initDataInputStream() throws IOException {
        byte[] buffer = new byte[1024];
        int read = inputStream.read(buffer);
        String data_str = new String(buffer, 0, read);
        data = data_str.split(",");
    }

    @Override
    public String getImei() {
        return data[1];
    }

    public List<DeviceTracking> decode() {
        List<DeviceTracking> deviceTrackings = new ArrayList<>();

        DeviceTracking deviceTracking = newTracking();
        try {
//            packageFlag = inP.splitedolar(data[0]);
            deviceTracking.setImei(device.getImei());   //data[1]
//            command = data[2];
            int eventCode = Integer.parseInt(data[3]);
            String message;
            switch (eventCode) {
                case 1:
                    message = "help me";
                    break;
                case 17:
                    message = "low battery";
                    break;
                case 18:
                    message = "Power Low";
                    break;
                case 19:
                    message = "speed";
                    break;
                case 146:
                    message = "ACCStart";
                    break;
                case 147:
                    message = "ACCStop";
                    break;
                default:
                    message = "";
                    break;
            }
            deviceTracking.setMessage(message);
            deviceTracking.setAltitude(Double.parseDouble(data[4]));
            deviceTracking.setLongitude(Double.parseDouble(data[5]));
            deviceTracking.setTracking_time(Tools.DateTime.getTimestampFromDate(data[6]));
//            deviceTracking.gpsStatus = data[7]; todo ??

            deviceTracking.setSatellites(Integer.parseInt(data[8]));


            deviceTracking.setEtatSignal(Integer.parseInt(data[9]));

            double speed = Double.parseDouble(data[10]);

            if (speed > 150) {
                deviceTracking.setGps_speed((int) speed);
                speed = -1;
//                deviceTracking.setInput(trame);
            }
            if (speed >= 300) {
                deviceTracking.setGps_speed((int) speed);
            } else if (speed >= 200 && deviceTracking.getAcc_status() == 0) {
                deviceTracking.setGps_speed((int) speed);
                speed = 0;
                deviceTracking.setInput("");
            }

            deviceTracking.setSpeed(speed);

//            deviceTracking.orientation = Integer.parseInt(data[11]);

            deviceTracking.setGps_hdop(Double.parseDouble(data[12]));

            deviceTracking.setAltitude(Double.parseDouble(data[13]));

//            deviceTracking.mileage = Integer.parseInt(data[14]);

//            deviceTracking.runtime = Integer.parseInt(data[15]);

//            String[] baseID = data[16].split("\\|");
//            deviceTracking.MCC = Integer.parseInt(baseID[0]);
//            deviceTracking.MNC = Integer.parseInt(baseID[1]);
//            deviceTracking.LAC = Integer.parseInt(baseID[2], 16);
//            deviceTracking.CI = Integer.parseInt(baseID[3], 16);

            String state = Tools.splitPutsM(Integer.toBinaryString(Integer.parseInt(data[17], 16)));
            char[] io_list = state.toCharArray();

//            int input1 = (int) io_list[0];
//            int input2 = (int) io_list[1];
//            int input3 = (int) io_list[2];
//            int input4 = (int) io_list[3];
//            int input5 = (int) io_list[4];
            int input6 = io_list[5];
            int input7 = io_list[6];
//            int input8 = (int) io_list[7];
//            int output1 = (int) io_list[8];
//            int output2 = (int) io_list[9];
//            int output3 = (int) io_list[10];
//            int output4 = (int) io_list[11];
//            int output5 = (int) io_list[12];
//            int output6 = (int) io_list[13];
//            int output7 = (int) io_list[14];
//            int output8 = (int) io_list[15];

            String[] AD = data[18].split("\\|");
//            double AD1 = (Integer.parseInt(AD[0], 16) * 3.3 * 2) / 4096;
//            double AD2 = ((Integer.parseInt(AD[1], 16) * 3.3 * 2) / 4096);
//            double AD3 = ((Integer.parseInt(AD[2], 16) * 3.3 * 2) / 4096);
//            double BatteryInt = (float) ((Integer.parseInt(AD[3], 16) * 3.3 * 2) / 4096);
            try {
                deviceTracking.setExternal_power((float) ((Integer.parseInt(AD[4], 16) * 3.3 * 16) / 4096));
            } catch (Exception e) {
                Console.printStackTrace(e);
                deviceTracking.setExternal_power((float) 0);
            }
            int acc = (input6 == 1 || input7 == 1) ? 1 : 0;
            deviceTracking.setAcc_status(acc);
//            if (eventCode == 20 || eventCode == 21) {
//                deviceTracking.fence = Integer.parseInt(data[19]);
//            }
            if (eventCode == 37) {
                deviceTracking.setRfid(String.valueOf(Integer.parseInt(data[19], 16)));
            }
//            if (eventCode == 39) {
//                deviceTracking.picture = data[19];
//            }

//            if (eventCode == 50 || eventCode == 51) {
//                deviceTracking.temperatureNmbre = Integer.parseInt(data[19]);
//            }
            if (data.length >= 21) {
                if (data.length == 24) {
                    FnSetReservoir(data[22], deviceTracking);
                    FnSetAllTemperature(data[16].split("\\|"), deviceTracking);

                } else {
                    if (data[22].length() == 4)
                        FnSetReservoir(data[22], deviceTracking);
                    else
                        FnSetAllTemperature(data[16].split("\\|"), deviceTracking);
                }

            }
        } catch (Exception e) {
            Console.printStackTrace(e);
            Console.println("----------------ERROR------------");
            Console.println("Meitrack : " + Arrays.toString(data));
            Console.println("-----------------------------------");
            Thread.currentThread().interrupt();
        }


        return deviceTrackings;
    }


    private void FnSetAllTemperature(String[] temperaturesData, DeviceTracking deviceTracking) {
        if (temperaturesData.length == 3) {
            deviceTracking.setTemperature(Integer.parseInt(temperaturesData[0].substring(2, 4), 16) + Integer.parseInt(temperaturesData[0].substring(4), 16) * 0.01);
            deviceTracking.setTemperature2(Integer.parseInt(temperaturesData[1].substring(2, 4), 16) + Integer.parseInt(temperaturesData[1].substring(4), 16) * 0.01);
//                        deviceTracking.setTemperature3 ( Integer.parseInt(temperatures[2].substring(2, 4), 16) + Integer.parseInt(temperatures[2].substring(4, 6), 16) * 0.01);
        }
    }

    private void FnSetReservoir(String reservoirData, DeviceTracking deviceTracking) {
        double reservoir1 = Integer.parseInt(reservoirData.substring(0, 2), 16) + Integer.parseInt(reservoirData.substring(2, 4), 16) * 0.01;
        deviceTracking.setReservoir1(reservoir1);
    }

}


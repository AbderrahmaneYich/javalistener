package app.decoder.basic;

import app.model.Device;
import app.model.DeviceTracking;
import app.tools.vondors.CodecException;
import config.ListDevices;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class DecoderDevice {
    public Device device = null;
    public int port;
    protected InputStream inputStream = null;
    protected Socket socket = null;
    protected OutputStream outputStream = null;
    protected DataInputStream dataInputStream = null;
    protected DataOutputStream dataOutputStream = null;

    //#1
    void setSocket(Socket socket) throws IOException {
        this.socket = socket;
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        dataInputStream = new DataInputStream(inputStream);
        dataOutputStream = new DataOutputStream(outputStream);
    }

    //#2
    public void initDataInputStream() throws IOException, CodecException {
    }

    //#3
    public String getImei() throws IOException {
        //return device imei
        return null;
    }

    //#5
    public void handshake() throws IOException {
//        send handshake to device to start sending the trackings if the device require that
    }

    //#6
    public void setDevice(Device device) {
        this.device = device;
    }

    //#7
    public List<DeviceTracking> decode() throws IOException, CodecException {
        //decoding the  devices tracking data to DeviceTracking objects
        return null;
    }


    public void feedBackOk(int nbrTracking) throws IOException {
        //feedBackOk to device show the TeltonikaDevice as Example
    }

    //    #4||
    void closeDataIOStream() throws IOException {
        if (dataOutputStream != null) {
            dataOutputStream.close();
        }
        if (dataInputStream != null) {
            dataInputStream.close();
        }
    }


    protected void closeIOStream() throws IOException {
        closeDataIOStream();
        inputStream.close();
        outputStream.close();
    }


    protected DeviceTracking newTracking() {
        DeviceTracking tracking = new DeviceTracking();
        tracking.deviceDetails = ListDevices.getDeviceConfigs(port);
        return tracking;
    }


}


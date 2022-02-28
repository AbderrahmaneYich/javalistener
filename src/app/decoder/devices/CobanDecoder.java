package app.decoder.devices;

import app.decoder.basic.DecoderDevice;
import app.model.DeviceTracking;
import app.tools.Console;
import app.tools.Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CobanDecoder extends DecoderDevice {


    private List<String> data = new ArrayList<>();
    private Keys keys = new Keys();

    @Override
    public void initDataInputStream() throws IOException {
        dataOutputStream.writeBytes("LOAD");
        BufferedReader bufferedReader;
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        cobanDecode(bufferedReader);
    }

    @Override
    public String getImei() {
        try {
            return data.get(keys.IMEI);
        } catch (Exception e) {
//            Console.printStackTrace(e);
            Console.println("Imei not fond  in data ");
            Console.println(data.toString());
            return "";
        }
    }

    @Override
    public List<DeviceTracking> decode() {
        List<DeviceTracking> deviceTrackings = new ArrayList<>();
        DeviceTracking deviceTracking = newTracking();
        deviceTracking.setBasicData(device.getImei(), device.getId(), device.getId_vehicule(), null);
        deviceTracking.setAcc_status(Integer.parseInt(data.get(keys.ACC)));
        deviceTracking.setAngle(Double.parseDouble(data.get(keys.DIRECTION)));
        deviceTracking.setTracking_time(Tools.DateTime.getTimestampFromDate(data.get(keys.TRACKING_SEND_DATETIME)));
        deviceTracking.setLongitude(Tools.GBS.toDD(data.get(keys.GPS_LONGITUDE), data.get(keys.GPS_W_E), keys.GPS_LATITUDE_IngDegreeEnd));
        deviceTracking.setLatitude(Tools.GBS.toDD(data.get(keys.GPS_LATITUDE), data.get(keys.GPS_N_S), keys.GPS_LONGITUDE_IngDegreeEnd));
        if (!data.get(keys.GPS_ALTITUDE).equals("")) {
            deviceTracking.setAltitude(Double.parseDouble(data.get(keys.GPS_ALTITUDE)));
        }
        deviceTracking.setMessage(data.get(keys.MESSAGE));
        // convert knote to KM
        double speedKnot = Double.parseDouble(data.get(keys.GPS_SPEED));
        double speed;
        if (speedKnot <= 1) {
            speed = 0;
        } else {
            speed = speedKnot * 1.852000;
            if (speed > 150) {
                deviceTracking.setGps_speed((int) speed);
                speed = -1;
            }
        }
        deviceTracking.setSpeed(speed);
        Matcher m_reservoir = Pattern.compile("([0-9]{0,3}\\.[0-9]{0,2})%").matcher(data.get(keys.RESERVOIR1));
        if (m_reservoir.find()) {
            deviceTracking.setReservoir1(Double.parseDouble(m_reservoir.group(1)));
        }
        if (!data.get(keys.TEMPERATURE).equals("")) {
            deviceTracking.setTemperature(Double.parseDouble(data.get(keys.TEMPERATURE)));
        }
        deviceTracking.setExternal_power(1);
        if (deviceTracking.getMessage().equals("ac alarm")) {
            deviceTracking.setExternal_power(0);
        }
        deviceTracking.setCurrent_battery(4.3);
        if (deviceTracking.getMessage().equals("low battery")) {
            deviceTracking.setCurrent_battery(0.5);
        }
        if (data.get(keys.GPS_QUALITY).compareTo("F") == 0) {
            deviceTracking.setEtatSignal(5);
            deviceTracking.setSatellites(7);
        } else if (data.get(keys.GPS_QUALITY).compareTo("L") == 0) {
            deviceTracking.setEtatSignal(1);
            deviceTracking.setSatellites(1);
        }
        deviceTrackings.add(deviceTracking);
        return deviceTrackings;
    }

    private void cobanDecode(BufferedReader bufferedReader) throws IOException {
        StringBuilder s = new StringBuilder();
        int i = bufferedReader.read();
        boolean isLoaded = false;
        while (i > 0 && !isLoaded) {
            char c = (char) i;
            s.append(c);
            if (c == ';') {
                if (Pattern.compile("##,imei:[0-9]*,A;").matcher(s).find()) {
                    dataOutputStream.writeBytes("LOAD");
                } else if (s.length() > 16) {
                    isLoaded = true;
                    data = Arrays.asList(s.toString().split(","));
                    Matcher m = Pattern.compile("[0-9]+").matcher(data.get(0));
                    if (m.find()) {
                        data.set(0, m.group());
                    }
                }
                s = new StringBuilder();
            }
            i = bufferedReader.read();

        }
    }


    private static class Keys {
        ////  coban
        int
//                SIM_ADMIN = 3,
//                TIME_TRACKING = 5,
//                GPS_QUALITY_2 = 6,
                DIRECTION = 12,
        //                DOOR = 15,
        // commune
        IMEI = 0,
                MESSAGE = 1,
                TRACKING_SEND_DATETIME = 2,
                GPS_QUALITY = 4,
                GPS_LATITUDE = 7,
                GPS_N_S = 8,
                GPS_LATITUDE_IngDegreeEnd = 3,
                GPS_LONGITUDE = 9,
                GPS_W_E = 10,
                GPS_LONGITUDE_IngDegreeEnd = 2,
                GPS_SPEED = 11,
                GPS_ALTITUDE = 13,
                ACC = 14,
                RESERVOIR1 = 16,
                TEMPERATURE = 17;
    }

}


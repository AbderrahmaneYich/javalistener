package app.decoder.devices;

import app.decoder.basic.DecoderDevice;
import app.model.DeviceTracking;
import app.tools.ByteWrapper;
import app.tools.CodecStore;
import app.tools.Console;
import app.tools.avldata.AvlData;
import app.tools.vondors.CodecException;
import app.tools.vondors.IOElement;
import config.ListDevices;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TeltonikaDecoder extends DecoderDevice {
    @Override
    public String getImei() throws IOException {
        return dataInputStream.readUTF();
    }

    @Override
    public void handshake() throws IOException {
        dataOutputStream.writeBoolean(true);
    }

    @Override
    public List<DeviceTracking> decode() throws IOException, CodecException {
        List<DeviceTracking> deviceTrackings = new ArrayList<>();
        byte[] packet = ByteWrapper.unwrapFromStream(dataInputStream);
        if (packet == null) {
            Console.println("Err : packet data is  null");
            return null;
        }
        AvlData decoder = CodecStore.getSuitableCodec(packet);
        if (decoder == null) {
            Console.println("Err : SuitableCodec not fond");
            return null;
        }
        AvlData[] avlDatas = decoder.decode(packet);
        for (AvlData avlData : avlDatas) {
            int dis_priority = avlData.getPriority();
            double dis_longitude = ((double) avlData.getGpsElement().getLongitude()) / 10000000;
            double dis_latitude = ((double) avlData.getGpsElement().getLatitude()) / 10000000;
            double dis_speed = avlData.getGpsElement().getSpeed();
            double dis_angle = avlData.getGpsElement().getAngle();
            double dis_altitude = avlData.getGpsElement().getAltitude();
            Integer dis_satellites = (int) (avlData.getGpsElement().getSatellites());
            String dis_tracking_time = new Timestamp(avlData.getTimestamp()).toString();
            DeviceTracking tracking = newTracking();
            tracking.setTracking_time(dis_tracking_time);
            tracking.setBasicData(device.getImei(), device.getId(), device.getId_vehicule(), dis_priority);
            tracking.setGPSTrackingData(dis_longitude, dis_latitude, dis_speed, dis_angle, dis_altitude, dis_satellites);

            fillData(tracking, avlData.getInputOutputElement()); //splitArray[7]
            deviceTrackings.add(tracking);
        }
        return deviceTrackings;
    }

    @Override
    public void feedBackOk(int nbrTracking) throws IOException {
        dataOutputStream.writeInt(nbrTracking);
    }

    private void fillData(DeviceTracking tracking, IOElement ioElement) {
        for (int key : ioElement.getAvailableProperties()) {
            double value = ioElement.getValue(key);
            tracking.setAvlData(key, value);
            switch (key) {
                case Keys.LVCAN_FUEL_LEVEL:
                case Keys.LVCAN_FUEL_CONSUMED:
                case Keys.LVCAN_FUEL_RATE:
                case Keys.LVCAN_ENGINE_TEMPERATURE:
                case Keys.TEMPERATURE:
                case Keys.TEMPERATURE2:
                    value = value / 10;
                    break;
                case Keys.EXTERNAL_POWER_VOLTAGE:
                case Keys.INTERNAL_BATTERY_VOLTAGE:
                    value = value / 1000.0;
                    break;
            }


            switch (key) {
                case Keys.ACC:
                    tracking.setAcc_status((int) value);
                    break;
                case Keys.ACTUAL_PROFILE:
                    tracking.setActual_profile((int) value);
                    break;
                case Keys.AREA_CODE:
                    tracking.setArea_code((int) value);
                    break;
                case Keys.CURRENT_OPERATOR_CODE:
                    tracking.setOperator_code((int) value);
                    break;
                case Keys.EXTERNAL_POWER_VOLTAGE:
                    tracking.setExternal_power(value);
                    tracking.setCurrent_battery(value);
                    break;
                case Keys.GNSS_STATUS:
                    tracking.setGnss_status((int) value);
                    break;
                case Keys.GPS_HDOP:
                    tracking.setGps_hdop(value);
                    break;
                case Keys.GSM_LEVEL:
                    tracking.setEtatSignal((int) value);
                    break;
                case Keys.INTERNAL_BATTERY_VOLTAGE:
                    tracking.setInternal_battery(value);
                    break;
                case Keys.MOUVEMENT:
                    tracking.setMovement((int) value);
                    break;
                case Keys.ODOMETER:
                    tracking.HavOdometer=true;
                    tracking.setKm(value);
                    break;
                case Keys.PCB_TEMPERATURE:
                    tracking.setPcb_temp(value);
                    break;
                case Keys.RFID:
                    tracking.setRfid(String.valueOf(value));
                    break;
                case Keys.SLEEP_MODE:
                    tracking.setSleep_mode((int) value);
                    break;
                case Keys.LVCAN_VEHICLE_SPEED:
                    tracking.LVCAN_VEHICLE_SPEED = value;
                    break;
                case Keys.LVCAN_ACCELERATOR_PEDAL_POSITION:
                    tracking.LVCAN_ACCELERATOR_PEDAL_POSITION = value;
                    break;
                case Keys.LVCAN_ENGINE_RPM:
                    tracking.LVCAN_ENGINE_RPM = (int) value;
                    break;
                case Keys.LVCAN_DOOR_STATUS:
                    tracking.LVCAN_DOOR_STATUS = (int) value;
                    break;
                case Keys.LVCAN_GREEN_DRIVING_TYPE:
                    tracking.GREEN_DRIVING_TYPE = value;
                    break;
                case Keys.CRASH_DETECTION:
                    tracking.CRASH_DETECTION = value;
                    break;
                case Keys.GREEN_DRIVING_VALUE:
                    tracking.GREEN_DRIVING_VALUE = value;
                    break;
                case Keys.LVCAN_JAMMING:
                    tracking.LVCAN_JAMMING = value;
                    break;
                case Keys.GREEN_DRIVING_EVENT_DURATION:
                    tracking.GREEN_DRIVING_EVENT_DURATION = value;
                    break;
                case Keys.LVCAN_ENGINE_TEMPERATURE:
                    if (value > 0)
                        tracking.LVCAN_ENGINE_TEMPERATURE = value;
                    break;
                case Keys.TEMPERATURE:
                    if (value / 10 < 300)
                        tracking.setTemperature(value);
                    break;
                case Keys.TEMPERATURE2:
                    if (value / 10 < 300)
                        tracking.setTemperature2(value);
                    break;
                case Keys.LVCAN_TOTAL_MILEAGE:
                    tracking.LVCAN_TOTAL_MILEAGE = value;
//                    if (tracking.deviceDetails == ListDevices.teltonika3) {
//                        tracking.getPrevious();
//                        if (tracking.getPrevious() != null && tracking.LVCAN_TOTAL_MILEAGE >= tracking.getPrevious().LVCAN_TOTAL_MILEAGE)
//                            tracking.setKm(tracking.LVCAN_TOTAL_MILEAGE - tracking.getPrevious().LVCAN_TOTAL_MILEAGE);
//                    }
                    break;

                case Keys.ENGINE_OIL_LEVEL:
                    tracking.ENGINE_OIL_LEVEL = value;
                    break;
                case Keys.LVCAN_FUEL_RATE:
                    tracking.LVCAN_FUEL_RATE = value;
                    break;
                case Keys.VOL_TANK_1:
                    if (tracking.deviceDetails == ListDevices.teltonika3)//teltonika3 port:3462
                        tracking.setReservoir1(value);
                    break;
                case Keys.LVCAN_FUEL_LEVEL:
                    if (tracking.deviceDetails == ListDevices.teltonika4)//teltonika3 port:3462
                        tracking.setReservoir1(value);
                    break;
                case Keys.AI_1:
                    if (tracking.deviceDetails != ListDevices.teltonika3)//teltonika3 port:3462
                        if (device.volReservoir1Max > 0 && device.volReservoir1Min >= 0) {
                            float reservoir = (float) (((value - device.volReservoir1Min) / (device.volReservoir1Max - device.volReservoir1Min)) * 100);
                            tracking.setReservoir1(reservoir);
                        }
                    break;
                case Keys.AI_2:
                    if (device.volReservoir2Max > 0 && device.volReservoir2Min >= 0) {
                        float reservoir = (float) (((value - device.volReservoir2Min) / (device.volReservoir2Max - device.volReservoir2Min)) * 100);
                        tracking.setReservoir2(reservoir);
                    }
                    break;
            }
        }

        if (ioElement.getProperty(Keys.ODOMETER) == null) {//todo who(port) need  this check
            tracking.setMessage("ODOMETER not activated");
        }
    }

    @Override
    protected void closeIOStream() throws IOException {
        if (dataOutputStream != null) {
            dataOutputStream.writeInt(0);
            dataOutputStream.close();
        }
        if (dataInputStream != null) {
            dataInputStream.close();
        }
        inputStream.close();
        outputStream.close();
    }

    private static final class Keys {
        private static final int
                ACC = 1,
                MOUVEMENT = 240,
                GSM_LEVEL = 21,
        //                GPS_PDOP = 181,
        GPS_HDOP = 182,
        //                CELL_ID = 205,
        AREA_CODE = 206,
                ODOMETER = 199,
                CURRENT_OPERATOR_CODE = 241,
                RFID = 207,
                ACTUAL_PROFILE = 22,
                EXTERNAL_POWER_VOLTAGE = 66,
                INTERNAL_BATTERY_VOLTAGE = 67,
        //                INTERNAL_BATTERY_CURRENT = 68,
        PCB_TEMPERATURE = 70,
                SLEEP_MODE = 200,
                GNSS_STATUS = 71,
        //                SPEEDOMETER = 24,
        AI_1 = 9,//Reservoir 1
                AI_2 = 10,//Reservoir 2
        //                AI_3 = 11,//Temperature 1
//                AI_4 = 19,//Temperature 2
        TEMPERATURE = 72,
                TEMPERATURE2 = 73,
                LVCAN_VEHICLE_SPEED = 81,
                LVCAN_ACCELERATOR_PEDAL_POSITION = 82,
                LVCAN_FUEL_CONSUMED = 83,
                LVCAN_FUEL_LEVEL = 84,
                LVCAN_ENGINE_RPM = 85,
                LVCAN_TOTAL_MILEAGE = 87,
                VOL_TANK_1 = 89,
                LVCAN_DOOR_STATUS = 90,
                LVCAN_FUEL_RATE = 110,
                LVCAN_ENGINE_TEMPERATURE = 115,
                ENGINE_OIL_LEVEL = 235,
                LVCAN_GREEN_DRIVING_TYPE = 253,
                CRASH_DETECTION = 247,
                GREEN_DRIVING_VALUE = 254,
                LVCAN_JAMMING = 249,
                GREEN_DRIVING_EVENT_DURATION = 243;
//              LVCAN_ENGINE_WORKTIME = 102,
//              LVCAN_ENGINE_WORKTIME_COUNTED = 103,
    }
}


package app.decoder.devices;

import app.decoder.basic.DecoderDevice;
import app.model.DeviceTracking;
import config.Env;

import javax.xml.bind.DatatypeConverter;
import java.io.DataInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RuptelaDecoder extends DecoderDevice {

    private static long readValue(DataInputStream buf, int length, boolean signed) throws IOException {
        switch (length) {
            case 1:
                return signed ? buf.readByte() : buf.readUnsignedByte();
            case 2:
                return signed ? buf.readShort() : buf.readUnsignedShort();
            case 4:
                return signed ? buf.readInt() : buf.readInt();
            default:
                return buf.readLong();
        }
    }

    public void initDataInputStream() throws IOException {
        dataInputStream.readUnsignedShort();//skip UnsignedShort if  ruptela
    }

    @Override
    public String getImei() throws IOException {
        return String.format("%015d", dataInputStream.readLong());
    }

    public List<DeviceTracking> decode() throws IOException {
        List<DeviceTracking> deviceTrackings = new ArrayList<>();
        int type = dataInputStream.readUnsignedByte();
        if (type == Env.MSG.RECORDS || type == Env.MSG.EXTENDED_RECORDS) {
            dataInputStream.readUnsignedByte();//skip UnsignedByte
            int count = dataInputStream.readUnsignedByte();
//            Console.totalTrackingElements(count);
            for (int i = 0; i < count; i++) {
                //read Data from input stream/////////////////////////////////////////////////////////////////////////////
                long dis_tracking_time = dataInputStream.readInt();
                dataInputStream.readUnsignedByte();//skip UnsignedByte
                if (type == Env.MSG.EXTENDED_RECORDS) {
                    dataInputStream.readUnsignedByte();//skip UnsignedByte
                }
                dataInputStream.readUnsignedByte();//skip UnsignedByte
                double dis_longitude = dataInputStream.readInt();
                double dis_latitude = dataInputStream.readInt();
                double dis_altitude = dataInputStream.readUnsignedShort();
                double dis_angle = dataInputStream.readUnsignedShort();
                int dis_satellites = dataInputStream.readUnsignedByte();
                double dis_speed = dataInputStream.readUnsignedShort();
                int dis_gps_hdop = dataInputStream.readUnsignedByte();
                //end read Data from input stream/////////////////////////////////////////////////////////////////////////
                DeviceTracking tracking = newTracking();
                tracking.setBasicData(
                        device.getImei(),
                        device.getId(),
                        device.getId_vehicule(),
                        null
                );
                tracking.setGPSTrackingData(
                        dis_longitude / 10000000.0,
                        dis_latitude / 10000000.0,
                        dis_speed * 0.539957,
                        dis_angle / 100,
                        dis_altitude / 10,
                        dis_satellites
                );
                tracking.setTracking_time(new Timestamp(dis_tracking_time * 1000).toString());
                tracking.setGps_hdop(dis_gps_hdop);

                if (type == Env.MSG.EXTENDED_RECORDS) {
                    dataInputStream.readUnsignedShort();//skip UnsignedShort
                } else {
                    dataInputStream.readUnsignedByte();//skip UnsignedByte
                }
                decodeParameters(tracking, dataInputStream, 1, type);
                decodeParameters(tracking, dataInputStream, 2, type);
                decodeParameters(tracking, dataInputStream, 4, type);
                decodeParameters(tracking, dataInputStream, 8, type);
                deviceTrackings.add(tracking);
            }
        }
        return deviceTrackings;
    }

    @Override
    public void feedBackOk(int nbrTracking) throws IOException {
        dataOutputStream.write(DatatypeConverter.parseHexBinary("0002640113bc"));
    }

    private void decodeParameters(DeviceTracking tracking, DataInputStream dataInputStream, int length, int type) throws IOException {
        int cnt = dataInputStream.readUnsignedByte();
        for (int j = 0; j < cnt; j++) {
            int id = type == Env.MSG.EXTENDED_RECORDS ? dataInputStream.readUnsignedShort() : dataInputStream.readUnsignedByte();
            decodeParameter(tracking, id, dataInputStream, length);
        }
    }

    public void decodeParameter(DeviceTracking tracking, int id, DataInputStream dataInputStream, int length) throws IOException {
        long value;
        switch (id) {
            case 74:
            case 78:
            case 79:
            case 80:
                value = readValue(dataInputStream, length, true);
                break;
            default:
                value = readValue(dataInputStream, length, false);

        }
        switch (id) {
//            case 2:
//            case 3:
//            case 4:
//                Console.println("di" + (id - 1) + " : " + readValue(dataInputStream, length, false));
//                return;
            case 5:
                int acc = ((value == 1) ? 1 : 0) != tracking.getAcc_status() ? 1 : 0;
                tracking.setAcc_status(acc);
                break;
            case 22:
                if (device.volReservoir1Max > 0 && device.volReservoir1Min >= 0) {
                    float reservoir = ((value - device.volReservoir1Min) / (device.volReservoir1Max - device.volReservoir1Min)) * 100;
                    tracking.setReservoir1(reservoir);
                }
                break;
            case 23:
                if (device.volReservoir2Max > 0 && device.volReservoir2Min >= 0) {
                    float reservoir = ((value - device.volReservoir2Min) / (device.volReservoir2Max - device.volReservoir2Min)) * 100;
                    tracking.setReservoir2(reservoir);
                }
                break;
            case 27:
                tracking.setEtatSignal(Math.toIntExact(value));//signal level
                break;
            case 29:
                tracking.setExternal_power(value / 1000.0);
                break;
            case 30:
                tracking.setCurrent_battery(value / 1000.0);//battery interne
                break;
            case 176:
                tracking.setSpeed(value);
                break;
            case 74:
                tracking.setAvlData(id, (value * 0.1));
//                Console.println("PREFIX_TEMP " + 3 + " : " + value* 0.1);
                break;
            case 78:
            case 79:
            case 80:
                tracking.setAvlData(id, (value * 0.1));
//                Console.println("PREFIX_TEMP " + (id - 78) + " : " + value * 0.1);
                return;
            default:
                tracking.setAvlData(id, value);
        }
    }

}


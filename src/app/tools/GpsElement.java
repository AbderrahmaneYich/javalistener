package app.tools;


import app.tools.vondors.BitInputStream;
import app.tools.vondors.BitOutputStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * <p>
 * Class containing gps information
 * </p>
 */
public class GpsElement {

    /**
     * Value by which the longitude and latitude coordinates are multiplied (no float type)
     */
    public static final long WGS_PRECISION = 10000000;

    /**
     * X coordinate in WGS format, multiplied by WGS_PRECISION
     */
    private int longitude = 0;

    /**
     * Y coordinate in WGS format, multiplied by WGS_PRECISION
     */
    private int latitude = 0;

    /**
     * Speed in km/h
     */
    private short speed = 255;

    /**
     * Direction in degrees, 0 equals north, increasing clockwise (45 equals
     * north east)
     */
    private short angle = 0;

    /**
     * Altitude in meters from see level
     */
    private short altitude = 0;

    /**
     * Number of visible satellites
     */
    private byte satellites;

    /**
     * Empty default constructor
     */
    public GpsElement() {
        // Empty default constructor
    }

    /**
     * Creates new GPS data with values
     *
     * @param longitude  X coordinate in WGS format multiplied by WGS_PRECISION
     * @param latitude   Y coordinate in WGS format multiplied by WGS_PRECISION
     * @param altitude   Altitude in meters from sea level
     * @param angle      Angle of movement 0 equals NORTH increasing clockwise
     * @param satellites Number of visible satellites max 12
     * @param speed      Speed in km/h
     * @throws IllegalAccessException If any of passed parameters is illegal, see setters
     */
    private GpsElement(int longitude, int latitude, short altitude, short angle, byte satellites, short speed) {
        setLongitude(longitude);
        setLatitude(latitude);
        setAltitude(altitude);
        setAngle(angle);
        setSatellites(satellites);
        setSpeed(speed);
    }

    /**
     * Reads GPS element from codec input stream, written by write method
     *
     * @param inputStream Input stream to read from
     * @return Returns read GPS element
     * @throws IOException Thrown by passed input stream, or if unable to read gps data
     *                     from input stream
     */
    public static GpsElement read(BitInputStream inputStream) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        int longitude = dataInputStream.readInt();
        int latitude = dataInputStream.readInt();
        short altitude = dataInputStream.readShort();
        short angle = dataInputStream.readShort();
        byte satellites = dataInputStream.readByte();
        short speed = dataInputStream.readShort();
        try {
            return new GpsElement(longitude, latitude, altitude, angle, satellites, speed);
        } catch (IllegalArgumentException e) {
            Console.printStackTrace(e);
            throw new IOException("Unable to read GPS element. " + e);
        }
    }

    /**
     * Writes GPS element to output stream. Can be read by read method
     *
     * @param outputStream Output stream to write to
     * @param gpsElement   GPS element to write
     * @throws IOException Thrown by passed output stream
     */
    public static void write(BitOutputStream outputStream, GpsElement gpsElement) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeInt(gpsElement.getLongitude());
        dataOutputStream.writeInt(gpsElement.getLatitude());
        dataOutputStream.writeShort(gpsElement.getAltitude());
        dataOutputStream.writeShort(gpsElement.getAngle());
        dataOutputStream.writeByte(gpsElement.getSatellites());
        dataOutputStream.writeShort(gpsElement.getSpeed());
        dataOutputStream.flush();
    }

    /**
     * @return Returns altitude in meters from see level
     */
    public short getAltitude() {
        return altitude;
    }

    /**
     * @param altitude Altitude in meters from see level
     */
    public void setAltitude(short altitude) {
        this.altitude = altitude;
    }

    /**
     * @return Returns direction in degrees, 0 equals north, increasing
     * clockwise (45 equals north east)
     */
    public short getAngle() {
        return angle;
    }

    /**
     * @param angle Direction in degrees, 0 equals north, increasing clockwise
     *              (45 equals north east)
     * @throws IllegalArgumentException if angle is less 0 and greater than 360
     */
    public void setAngle(short angle) {
        if (angle < 0 || angle > 360) {
            throw new IllegalArgumentException("Acceptable angle is [0..360 " + angle);
        }
        this.angle = angle;
    }

    /**
     * @return Returns Y coordinate in WGS format, multiplied by WGS_PRECISION
     */
    public int getLatitude() {
        return latitude;
    }

    /**
     * @param latitude Y coordinate in WGS format, multiplied by WGS_PRECISION latitude to
     *                 set.
     * @throws IllegalArgumentException If latitude is less than -90 * WGS_PRECISION and greater than 90 *
     *                                  WGS_PRECISION
     */
    public void setLatitude(int latitude) {
        if (latitude < -90 * WGS_PRECISION || latitude > 90 * WGS_PRECISION) {
            throw new IllegalArgumentException("Acceptable latitude value is [" + (-90 * WGS_PRECISION) + ".." + (90 * WGS_PRECISION) + "] " + latitude);
        }
        this.latitude = latitude;
    }

    /**
     * @return Returns number of visible satellites
     */
    public byte getSatellites() {
        return satellites;
    }

    /**
     * @param satellites Number of visible satellites
     * @throws IllegalArgumentException If satellites is negative or greater than 12
     */
    public void setSatellites(byte satellites) {
        this.satellites = satellites;
    }

    /**
     * @return Returns speed in km/h
     */
    public short getSpeed() {
        return speed;
    }

    /**
     * @param speed Speed in km/h
     * @throws IllegalArgumentException If speed is negative or greater than 255
     */
    public void setSpeed(short speed) {
        this.speed = speed;
    }

    /**
     * @return Returns X coordinate in WGS format, multiplied by WGS_PRECISION
     */
    public int getLongitude() {
        return longitude;
    }

    /**
     * @param longitude X coordinate in WGS format, multiplied by WGS_PRECISION
     * @throws IllegalArgumentException If longitude is less than -180 * WGS_PRECISION and greater than 180 *
     *                                  WGS_PRECISION
     */
    public void setLongitude(int longitude) {
        if (longitude < -180 * WGS_PRECISION || longitude > 180 * WGS_PRECISION) {
            throw new IllegalArgumentException("Acceptable longitude value is [" + (-180 * WGS_PRECISION) + ".." + (180 * WGS_PRECISION) + "] " + longitude);
        }
        this.longitude = longitude;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        //return "[X=" + getLongitude() + "] [Y=" + getLatitude() + "] [Speed=" + getSpeed() + "] [Angle=" + getAngle() + "] [Altitude=" + altitude + "] [Satellites=" + getSatellites() + "]";
        return getLongitude() + "," + getLatitude() + "," + getSpeed() + "," + getAngle() + "," + getAltitude() + "," + getSatellites();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return getLongitude() + getLatitude() + getSpeed() + getSatellites();
    }


    /* (non-Javadoc)
     * @see de.enough.polish.io.Externalizable#read(java.io.DataInputStream)
     */
    public void read(DataInputStream in) throws IOException {
        setLongitude(in.readInt());
        setLatitude(in.readInt());
        setAltitude(in.readShort());
        setAngle(in.readShort());
        setSatellites(in.readByte());
        setSpeed(in.readShort());
    }

    /* (non-Javadoc)
     * @see de.enough.polish.io.Externalizable#write(java.io.DataOutputStream)
     */
    public void write(DataOutputStream out) throws IOException {
        out.writeInt(getLongitude());
        out.writeInt(getLatitude());
        out.writeShort(getAltitude());
        out.writeShort(getAngle());
        out.writeByte(getSatellites());
        out.writeShort(getSpeed());
    }
}

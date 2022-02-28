package app.tools.avldata;


import app.tools.Console;
import app.tools.GpsElement;
import app.tools.vondors.BitInputStream;
import app.tools.vondors.BitOutputStream;
import app.tools.vondors.CodecException;
import app.tools.vondors.IOElement;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class AvlData {
    /**
     * Instance of data codec used to encode/decode this data element
     */
    private static AvlData dataCodec = null;
    /**
     * DateTime stamp when data was acquired
     */
    private long timestamp = 0;

    /**
     * Message priority. Unsigned value. Higher the value higher the priority
     */
    private byte priority = Priority.NORMAL;

    /**
     * Information about status of input and output values
     */
    private IOElement inputOutputElement = new IOElement();

    /**
     * Position information
     */
    private GpsElement gpsElement = new GpsElement();

    //  constructor
    protected AvlData() {
        // empty constructor
    }

    public AvlData(long timestamp, GpsElement gpsElement, IOElement ioElement, byte priority) {
        setTimestamp(timestamp);
        setGpsElement(gpsElement);
        setInputOutputElement(ioElement);
        setPriority(priority);
    }

    /**
     * @return Returns DataCodec for encoding/decoding this data element
     */
    public static AvlData getCodec() {
        if (dataCodec == null) {
            dataCodec = new AvlData();
        }
        return dataCodec;
    }
    //END  constructor

    // getters & setters
    public GpsElement getGpsElement() {
        return gpsElement;
    }

    public void setGpsElement(GpsElement gpsData) {
        this.gpsElement = gpsData;
    }

    public IOElement getInputOutputElement() {
        return inputOutputElement;
    }

    public void setInputOutputElement(IOElement inputOutputElement) {
        this.inputOutputElement = inputOutputElement;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }
// end  getters & setters

    /**
     * Number of maximum elements encoded is 255
     */
    public byte[] encode(AvlData[] datas) throws CodecException {
        if (datas.length > 255) {
            throw new CodecException("Maximum elements is 255");
        }

        BitOutputStream codecOutputStream = new BitOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(codecOutputStream);

        try {
            dataOutputStream.writeByte(getCodecId());
            dataOutputStream.writeByte(datas.length);
            for (AvlData avlData : datas) {
                write(codecOutputStream, avlData);
            }
            dataOutputStream.writeByte(datas.length);
            dataOutputStream.flush();
        } catch (IOException e) {
            Console.printStackTrace(e);
            throw new CodecException("Unable to encode data. ", e);
        }

        return codecOutputStream.getByteArray();
    }

    public int encode(AvlData[] datas, int length, byte[] buffer, int offsetInBuffer) throws CodecException {
        if (length > 255) {
            throw new CodecException("Maximum elements is 255");
        }


        BitOutputStream codecOutputStream = new BitOutputStream(buffer, offsetInBuffer);
        DataOutputStream dataOutputStream = new DataOutputStream(codecOutputStream);

        try {
            dataOutputStream.writeByte(getCodecId());
            dataOutputStream.writeByte(length);
            for (int i = 0; i < length; i++) {
                write(codecOutputStream, datas[i]);
            }
            dataOutputStream.writeByte(length);
            dataOutputStream.flush();
        } catch (IOException e) {
            Console.printStackTrace(e);
            throw new CodecException("Unable to encode data. ", e);
        }

        return codecOutputStream.getWritenDataLength();
    }

    /**
     * This method writes passed data to passed output stream
     *
     * @param codecOutputStream Where to write
     * @param dataElement       What to write
     * @throws IOException If writing failed
     */
    protected void write(BitOutputStream codecOutputStream, AvlData dataElement) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(codecOutputStream);
        dataOutputStream.writeLong(dataElement.getTimestamp());
        dataOutputStream.writeByte(dataElement.getPriority());
        dataOutputStream.flush();
        GpsElement.write(codecOutputStream, dataElement.getGpsElement());
        IOElement.write(codecOutputStream, dataElement.getInputOutputElement());

    }

    public AvlData[] decode(byte[] dataByteArray) throws CodecException {
        BitInputStream codecInputStream = new BitInputStream(dataByteArray);
        DataInputStream dataInputStream = new DataInputStream(codecInputStream);
        AvlData[] result;
        try {
            if (dataInputStream.readByte() != getCodecId()) {
                throw new CodecException("Invalid codec id");
            }

            int elements = dataInputStream.readByte() & 0xFF;
            result = new AvlData[elements];
            for (int i = 0; i < elements; i++) {
                result[i] = read(codecInputStream);
            }
            if ((dataInputStream.readByte() & 0xFF) != elements) {
                throw new CodecException("Unable to decode.");
            }
        } catch (IOException e) {
            Console.printStackTrace(e);
            throw new CodecException("Unable to decode. ", e);
        }

        return result;
    }

    /**
     * This method reads Avl data from passed codec input stream
     *
     * @param codecInputStream Where from to read data
     * @return Read data
     * @throws IOException If unable to read data
     */
    protected AvlData read(BitInputStream codecInputStream) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(codecInputStream);
        long timestamp = dataInputStream.readLong();
        byte priority = dataInputStream.readByte();
        GpsElement gpsData = GpsElement.read(codecInputStream);
        IOElement iodata = IOElement.read(codecInputStream);

        return new AvlData(timestamp, gpsData, iodata, priority);
    }

    public byte getCodecId() {
        return 1;
    }

    public Class getCodecClass() {
        return getClass();
    }

    public String toString() {
        return getPriority() + "," + getGpsElement() + "," + getInputOutputElement() + "," + getTimestamp() + "#";
    }

    class Priority {
        //        static final byte HIGH = 20;
//        static final byte ABOVE_NORMAL = 15;
        static final byte NORMAL = 10;
//        static final byte BELLOW_NORMAL = 5;
//        static final byte LOW = 0;
    }

}

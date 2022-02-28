package app.tools.avldata;


import app.tools.GpsElement;
import app.tools.LongIOElement;
import app.tools.vondors.BitInputStream;
import app.tools.vondors.IOElement;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Ernestas Vaiciukevicius (ernestas.vaiciukevicius@lt)
 *
 * <p>Implementation of data codec used in FM4 modules.</p>
 */
public class AvlDataFM4 extends AvlData {
    /**
     * Instance of data codec used to encode/decode this data element
     */
    private static AvlData dataCodec = null;
    private int eventSource = 0;

    public AvlDataFM4() {
    }


    public AvlDataFM4(long timestamp, GpsElement gpsElement, IOElement ioElement, byte priority, int eventSourceId) {
        super(timestamp, gpsElement, ioElement, priority);
        this.eventSource = eventSourceId;
    }

    protected static LongIOElement readIOElement(BitInputStream codecInputStream) throws IOException {
        DataInputStream dis = new DataInputStream(codecInputStream);

        LongIOElement ret = new LongIOElement();
        int totalProperties = dis.readUnsignedByte();
        int propertiesRead = 0;

        // read one-byte properties
        int oneByteProperties = dis.readUnsignedByte();
        for (int i = 0; i < oneByteProperties; ++i) {
            int id = dis.readUnsignedByte();
            int value = dis.readByte();
            ret.addProperty(new int[]{id, value});
            ++propertiesRead;
        }

        // read two-byte properties
        int twoByteProperties = dis.readUnsignedByte();
        for (int i = 0; i < twoByteProperties; ++i) {
            int id = dis.readUnsignedByte();
            int value = dis.readShort();
            ret.addProperty(new int[]{id, value});
            ++propertiesRead;
        }

        // read four-byte properties
        int fourByteProperties = dis.readUnsignedByte();
        for (int i = 0; i < fourByteProperties; ++i) {
            int id = dis.readUnsignedByte();
            int value = dis.readInt();
            ret.addProperty(new int[]{id, value});
            ++propertiesRead;
        }

        // read eight-byte properties
        int eightByteProperties = dis.readUnsignedByte();
        for (int i = 0; i < eightByteProperties; ++i) {
            int id = dis.readUnsignedByte();
            long value = dis.readLong();
            ret.addLongProperty(new long[]{id, value});
            ++propertiesRead;
        }

        if (totalProperties != propertiesRead) {
            throw new IOException("Wrong totalProperties field");
        }

        return ret;
    }

    /**
     * @return Returns DataCodec for encoding/decoding this data element
     */
    public static AvlData getCodec() {
        if (dataCodec == null) {
            dataCodec = new AvlDataFM4();

        }

        return dataCodec;
    }

    protected AvlData read(BitInputStream codecInputStream) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(codecInputStream);
        long timestamp = dataInputStream.readLong();
        byte priority = dataInputStream.readByte();
        GpsElement gpsData = GpsElement.read(codecInputStream);

        int eventSourceId = 0xFF & codecInputStream.read();

        LongIOElement iodata = readIOElement(codecInputStream);

        AvlDataFM4 ret = new AvlDataFM4(timestamp, gpsData, iodata, priority, eventSourceId);

        return ret;
    }

    public byte getCodecId() {
        return 8;
    }

    public int getTriggeredPropertyId() {
        return eventSource;
    }

    public String toString() {
        return getPriority() + "," + getGpsElement() + "," + getInputOutputElement() + "," + getTimestamp() + "#";
    }

}

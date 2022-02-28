package app.tools;

import app.tools.avldata.AvlData;

import java.util.ArrayList;

public class CodecStore {
    private static final ArrayList<AvlData> registeredCodecs = new ArrayList<>();

    /**
     * Registers codec
     *
     * @param dataCodec Codec to register
     */
    public static void register(AvlData dataCodec) {
        registeredCodecs.add(dataCodec);
        Console.println("Registered codec (id=" + dataCodec.getCodecId() + ") :" + dataCodec);
    }


    /**
     * Returns suitable codec to decode byte buffer
     *
     * @param dataToDecode Byte buffer to decode
     * @return Returns dataCodec or null if no suitable codec found
     */
    public static synchronized AvlData getSuitableCodec(byte[] dataToDecode) {
        if (dataToDecode == null)
            return null;
        for (AvlData codec : registeredCodecs) {
            if (dataToDecode[0] == codec.getCodecId()) {
                return codec;
            }
        }
        return null;
    }
}

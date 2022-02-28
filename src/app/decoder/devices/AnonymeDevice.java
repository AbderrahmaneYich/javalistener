package app.decoder.devices;

import app.decoder.basic.DecoderDevice;
import app.model.DeviceTracking;
import app.tools.vondors.CodecException;

import java.io.IOException;
import java.util.List;

public class AnonymeDevice extends DecoderDevice {

    @Override
    public void initDataInputStream() throws IOException, CodecException {
        super.initDataInputStream();
    }

    @Override
    public String getImei() throws IOException {
        return super.getImei();
    }

    @Override
    public void handshake() throws IOException {
        super.handshake();
    }


    @Override
    public List<DeviceTracking> decode() throws IOException, CodecException {
        return super.decode();
    }

    @Override
    public void feedBackOk(int nbrTracking) throws IOException {
        super.feedBackOk(nbrTracking);
    }


}

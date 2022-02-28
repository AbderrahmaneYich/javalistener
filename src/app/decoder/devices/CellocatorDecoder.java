package app.decoder.devices;

import app.decoder.basic.DecoderDevice;
import app.model.DeviceTracking;
import app.tools.Console;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CellocatorDecoder extends DecoderDevice {
    JsonParser jsonParser = new JsonParser();
    JsonObject jsonObject;

    public void initDataInputStream() throws IOException {
        String data = new BufferedReader(new InputStreamReader(inputStream)).readLine();
        int hashPos = data.indexOf("#");
        if (hashPos >= 0) {
            data = data.substring(hashPos + 1);
        }
//        Console.print(data);
        try {
            jsonObject = jsonParser.parse(data).getAsJsonObject();
        } catch (Exception e) {
            Console.print(data);
//            Console.printStackTrace(e);
        }
    }

    @Override
    public String getImei() {
        return jsonObject.getAsJsonObject("avlData").get("unitId").getAsString();
    }

    public List<DeviceTracking> decode() {
        List<DeviceTracking> deviceTrackings = new ArrayList<>();
        DeviceTracking tracking = newTracking();
        tracking.setBasicData(getImei(), device.getId(), device.getId_vehicule(), null);
        tracking.setGPSTrackingData(
                jsonObject.get("longitude").getAsDouble(),
                jsonObject.get("latitude").getAsDouble(),
                jsonObject.get("speed").getAsDouble(),
                jsonObject.get("angle").getAsDouble(),
                jsonObject.get("altitude").getAsDouble(),
                jsonObject.get("satellites").isJsonNull() ? null : jsonObject.get("satellites").getAsInt()
        );
        tracking.setKm(jsonObject.get("km").getAsDouble());
        tracking.setTracking_time(jsonObject.get("tracking_time").getAsString().substring(0, 19).replace("T", " "));
        tracking.setExternal_power(jsonObject.get("external_power").isJsonNull() ? 0 : jsonObject.get("external_power").getAsDouble());
        tracking.setInternal_battery(jsonObject.get("internal_battery").isJsonNull() ? 0 : jsonObject.get("internal_battery").getAsDouble());
        tracking.setAcc_status(jsonObject.getAsJsonObject("avlData").get("statusInputIgnition").getAsBoolean() ? 1 : 0);





        tracking.LVCAN_ACCELERATOR_PEDAL_POSITION=jsonObject.get("LVCAN_ACCELERATOR_PEDAL_POSITION").isJsonNull() ? 0 : jsonObject.get("LVCAN_ACCELERATOR_PEDAL_POSITION").getAsDouble();
        tracking.LVCAN_FUEL_CONSUMED=jsonObject.get("LVCAN_FUEL_CONSUMED").isJsonNull() ? 0 : jsonObject.get("LVCAN_FUEL_CONSUMED").getAsDouble();
        tracking.LVCAN_FUEL_LEVEL=jsonObject.get("LVCAN_FUEL_LEVEL").isJsonNull() ? 0 : jsonObject.get("LVCAN_FUEL_LEVEL").getAsDouble();
        tracking.LVCAN_ENGINE_RPM=jsonObject.get("LVCAN_ENGINE_RPM").isJsonNull() ? 0 : jsonObject.get("LVCAN_ENGINE_RPM").getAsInt();
        tracking.LVCAN_TOTAL_MILEAGE=jsonObject.get("LVCAN_TOTAL_MILEAGE").isJsonNull() ? 0 : jsonObject.get("LVCAN_TOTAL_MILEAGE").getAsDouble();
        tracking.LVCAN_DOOR_STATUS=jsonObject.get("LVCAN_DOOR_STATUS").isJsonNull() ? 0 : jsonObject.get("LVCAN_DOOR_STATUS").getAsInt();
        tracking.LVCAN_FUEL_RATE=jsonObject.get("LVCAN_FUEL_RATE").isJsonNull() ? 0 : jsonObject.get("LVCAN_FUEL_RATE").getAsDouble();
        tracking.LVCAN_ENGINE_TEMPERATURE=jsonObject.get("LVCAN_ENGINE_TEMPERATURE").isJsonNull() ? 0 : jsonObject.get("LVCAN_ENGINE_TEMPERATURE").getAsDouble();
        tracking.ENGINE_OIL_LEVEL=jsonObject.get("LVCAN_ENGINE_OIL_LEVEL").isJsonNull() ? 0 : jsonObject.get("LVCAN_ENGINE_OIL_LEVEL").getAsDouble();
        tracking.GREEN_DRIVING_TYPE=jsonObject.get("LVCAN_GREEN_DRIVING_TYPE").isJsonNull() ? 0 : jsonObject.get("LVCAN_GREEN_DRIVING_TYPE").getAsDouble();
        tracking.CRASH_DETECTION=jsonObject.get("LVCAN_CRASH_DETECTION").isJsonNull() ? 0 : jsonObject.get("LVCAN_CRASH_DETECTION").getAsDouble();
        tracking.GREEN_DRIVING_VALUE=jsonObject.get("LVCAN_GREEN_DRIVING_VALUE").isJsonNull() ? 0 : jsonObject.get("LVCAN_GREEN_DRIVING_VALUE").getAsDouble();
        tracking.LVCAN_JAMMING=jsonObject.get("LVCAN_JAMMING").isJsonNull() ? 0 : jsonObject.get("LVCAN_JAMMING").getAsDouble();
        tracking.GREEN_DRIVING_EVENT_DURATION=jsonObject.get("LVCAN_GREEN_DRIVING_EVENT_DURATION").isJsonNull() ? 0 : jsonObject.get("LVCAN_GREEN_DRIVING_EVENT_DURATION").getAsDouble();


        tracking.avlData = jsonObject.get("avlData").getAsJsonObject();

        deviceTrackings.add(tracking);
        return deviceTrackings;
    }

    @Override
    protected void closeIOStream() throws IOException {
        super.closeIOStream();
    }


}


package config;

import app.decoder.devices.*;
import app.tools.Tools.DateTime.T;
import app.tools.trackingDebugger.debuggers.*;

import java.util.ArrayList;
import java.util.Arrays;

public class ListDevices {

//    public static final DeviceConfigs anonymeDevice = new DeviceConfigs(0, "null", null, false, null);
    /////////////////////////// v1  /////////////////////////////
    public static final DeviceConfigs xexun = new DeviceConfigs(
            10500, "xexun", new XexunDecoder(), true,
            Arrays.asList(
//                    new ExternalPowerOrSignalNullDebugger(),
                    new BlackZoneDebugger()
                    , new TrackingIsOldDebugger()
                    , new TrackingExistInDatabaseDebugger()
                    , new CamouflageDebugger(false)
                    , new SuspectTimeDebugger(3 * T.h)
            )
    );
    public static final DeviceConfigs xexun2 = new DeviceConfigs(
            10501, "xexun2", new XexunDecoder2(), true,
            Arrays.asList(
//                    new ExternalPowerOrSignalNullDebugger(),
                    new BlackZoneDebugger()
                    , new TrackingIsOldDebugger()
                    , new TrackingExistInDatabaseDebugger()
                    , new CamouflageDebugger(false)
                    , new SuspectTimeDebugger(3 * T.h)
            )
    );
    public static final DeviceConfigs xexunfactory = new DeviceConfigs(
            40001, "xexunfactory", new XexunDecoder(), true,
            Arrays.asList(
//                    new ExternalPowerOrSignalNullDebugger(),
                    new BlackZoneDebugger()
                    , new TrackingIsOldDebugger()
                    , new TrackingExistInDatabaseDebugger()
                    , new CamouflageDebugger(false)
                    , new SuspectTimeDebugger(3 * T.h)
            )
    );
    public static final DeviceConfigs coban = new DeviceConfigs(
            3457, "coban", new CobanDecoder(), true,
            Arrays.asList(
//                    new ExternalPowerOrSignalNullDebugger(),
                    new BlackZoneDebugger()
                    , new BlackZoneDebugger()
                    , new TrackingIsOldDebugger()
                    , new TrackingExistInDatabaseDebugger()
                    , new CamouflageDebugger(false)
                    , new SuspectTimeDebugger(3 * T.h)
            )
    );
    public static final DeviceConfigs cobanrem = new DeviceConfigs(
            6446, "cobanrem", new CobanDecoder(), true,
            Arrays.asList(
//                    new ExternalPowerOrSignalNullDebugger(),
                    new BlackZoneDebugger()
                    , new TrackingIsOldDebugger()
                    , new TrackingExistInDatabaseDebugger()
                    , new CamouflageDebugger(false)
                    , new SuspectTimeDebugger(3 * T.h)
            )
    );
    public static final DeviceConfigs meitrackFactory = new DeviceConfigs(
            6000, "meitrackFactory", new MeiTrackDecoder(), true,
            Arrays.asList(
//                    new ExternalPowerOrSignalNullDebugger(),
                    new BlackZoneDebugger()
                    , new TrackingIsOldDebugger()
                    , new TrackingExistInDatabaseDebugger()
                    , new CamouflageDebugger(false)
                    , new SuspectTimeDebugger(3 * T.h)
            )
    );
    /////////////////////////// v2  /////////////////////////////
    public static final DeviceConfigs ruptela = new DeviceConfigs(
            3458, "ruptela", new RuptelaDecoder(), true,
            Arrays.asList(
//                    new ExternalPowerOrSignalNullDebugger(),
                    new BlackZoneDebugger()
                    , new TrackingIsOldDebugger()
                    , new TrackingExistInDatabaseDebugger()
                    , new CamouflageDebugger(false)
                    , new SuspectTimeDebugger(3 * T.h)
            )
    );
    //
    public static final DeviceConfigs teltonikaAutoCan = new DeviceConfigs(
            3700, "teltonikaAutoCan", new TeltonikaDecoder(), true,
            Arrays.asList(
//                    new ExternalPowerOrSignalNullDebugger()
                    new BlackZoneDebugger()
                    , new TrackingIsOldDebugger()
                    , new TrackingExistInDatabaseDebugger()
                    , new CamouflageDebugger(false)
                    , new SuspectTimeDebugger(3 * T.h)
            )
    );
    public static final DeviceConfigs teltonika = new DeviceConfigs(
            3459, "teltonika", new TeltonikaDecoder(), true,
            Arrays.asList(
                    new ExternalPowerOrSignalNullDebugger()
                    , new BlackZoneDebugger()
                    , new TrackingIsOldDebugger()
                    , new TrackingExistInDatabaseDebugger()
                    , new CamouflageDebugger(false)
                    , new SuspectTimeDebugger(3 * T.h)
            )
    );
    public static final DeviceConfigs teltonikaTmp = new DeviceConfigs(
            3460, "teltonikaTmp", new TeltonikaDecoder(), true,
            Arrays.asList(
                    new ExternalPowerOrSignalNullDebugger()
                    , new BlackZoneDebugger()
                    , new TrackingIsOldDebugger()
                    , new TrackingExistInDatabaseDebugger()
                    , new CamouflageDebugger(false)
                    , new SuspectTimeDebugger(3 * T.h)
            )
    );
    public static final DeviceConfigs teltonika2 = new DeviceConfigs(
            3461, "teltonikaTmp", new TeltonikaDecoder(), true,
            Arrays.asList(
//                    new ExternalPowerOrSignalNullDebugger()
                    new BlackZoneDebugger()
                    , new TrackingIsOldDebugger()
                    , new TrackingExistInDatabaseDebugger()
                    , new CamouflageDebugger(true)
                    , new SuspectTimeDebugger(3 * T.h)
            )
    );
    public static final DeviceConfigs teltonika3 = new DeviceConfigs(
            3462, "teltonika3", new TeltonikaDecoder(), false,
            Arrays.asList(
//                    new ExternalPowerOrSignalNullDebugger()
                    new BlackZoneDebugger(),
                    new TrackingIsOldDebugger(),
                    new TrackingExistInDatabaseDebugger(),
                    new FixBrokenTrackingData(),
                    new CamouflageDebugger(false),
                    new SuspectTimeDebugger(3 * T.h)
            )
    );
    public static final DeviceConfigs teltonika4 = new DeviceConfigs(
            3463, "teltonika4", new TeltonikaDecoder(), false,
            Arrays.asList(
//                    new ExternalPowerOrSignalNullDebugger()
                    new BlackZoneDebugger(),
                    new TrackingIsOldDebugger(),
                    new TrackingExistInDatabaseDebugger(),
                    new FixBrokenTrackingData(),
                    new CamouflageDebugger(false),
                    new SuspectTimeDebugger(3 * T.h)
            )
    );
    public static final DeviceConfigs cellocator = new DeviceConfigs(//Node Port 4001
            3434, "cellocator", new CellocatorDecoder(), false,
            Arrays.asList(
                    new BlackZoneDebugger(),
                    new TrackingIsOldDebugger(),
                    new TrackingExistInDatabaseDebugger(),
//                    new FixBrokenTrackingData(),
//                    new CamouflageDebugger(false),
                    new SuspectTimeDebugger(3 * T.h)
            )
    );
//
//    public static final DeviceConfigs anonymeDevice = new DeviceConfigs(
//            9999, "AnonymeDevice", new AnonymeDevice(), true,
//            Collections.emptyList()
//    );
//    public static final DeviceConfigs deviceName = new DeviceConfigs(
//            portNumber, "deviceName", new DeviceDecoder(), true,
//            Arrays.asList(
//                    new ExternalPowerOrSignalNullDebugger(),
//                    new TrackingWithBrokenImportantData(),
//                    new BlackZoneDebugger(),
//                    new TrackingIsOldDebugger(),
//                    new TrackingExistInDatabaseDebugger(),
//                    new FixBrokenTrackingData(),
//                    new CamouflageDebugger(false),
//                    new SuspectTimeDebugger(3 * T.h),
//                    )
//    );


    /////////////////////////////////////////////////////////////////
    public static ArrayList<DeviceConfigs> devicesArray = new ArrayList<>(
            Arrays.asList(
                    /* *** Pack 1 ****/
                    xexun,
                    xexun2,
                    xexunfactory,
                    coban,
                    cobanrem,
                    meitrackFactory,
                    /* *** Pack 2 ****/
                    ruptela,
                    teltonika,
                    teltonikaTmp,
                    teltonika2,
                    teltonikaAutoCan,
                    teltonika3,
                    teltonika4,
                    cellocator
//                    anonymeDevice
            )
    );


    public static ArrayList<DeviceConfigs> openedPortDevices() {
        ArrayList<DeviceConfigs> activeDevices = (ArrayList<DeviceConfigs>) devicesArray.clone();
        activeDevices.removeIf(s -> !s.portState.equals("opened"));
        return activeDevices;
    }

    public static DeviceConfigs getDeviceConfigs(int port) {
        for (DeviceConfigs deviceConfigs : devicesArray) {
            if (deviceConfigs.port == port)
                return deviceConfigs;
        }
        return null;
    }


    public static String devicesPortState() {
        StringBuilder state = new StringBuilder();
        for (DeviceConfigs deviceConfigs : devicesArray) {
            state.append("[").append(deviceConfigs.port).append("->").append(deviceConfigs.portState).append("]");
        }
        return state.toString();
    }
}
package app.tools.trackingDebugger;

import app.model.DeviceTracking;
import app.tools.trackingDebugger.debuggers.BlackZoneDebugger;

//import Debugger;

public class TrackingDebugger {
    private static final BlackZoneDebugger blackZone = new BlackZoneDebugger();
    public DeviceTracking tracking;

    public TrackingDebugger(DeviceTracking tracking) {
        this.tracking = tracking;
    }


}

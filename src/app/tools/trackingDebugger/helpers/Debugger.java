package app.tools.trackingDebugger.helpers;

import app.model.DeviceTracking;

public class Debugger {
    protected String codeErr = null;

    public boolean hasErr(DeviceTracking tracking) {
        return false;
    }

    public DeviceTracking debugging(DeviceTracking tracking) {
        if (hasErr(tracking))
            tracking.errCodes.add(codeErr);
        return tracking;
    }
}

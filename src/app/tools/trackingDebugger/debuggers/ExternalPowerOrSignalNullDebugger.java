package app.tools.trackingDebugger.debuggers;

import app.model.DeviceTracking;
import app.tools.trackingDebugger.helpers.Debugger;
import app.tools.trackingDebugger.helpers.ErrCodeList;

public class ExternalPowerOrSignalNullDebugger extends Debugger {
    public ExternalPowerOrSignalNullDebugger() {
        codeErr = ErrCodeList.externalPowerORSignalNull;
    }

    @Override
    public boolean hasErr(DeviceTracking tracking) {
        return tracking.getExternal_power() == null || tracking.getEtatSignal() == null;
    }
}

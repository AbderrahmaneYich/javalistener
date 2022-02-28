package app.tools.trackingDebugger.debuggers;

import app.model.DeviceTracking;
import app.tools.Tools;
import app.tools.trackingDebugger.helpers.Debugger;
import app.tools.trackingDebugger.helpers.ErrCodeList;

public class TrackingExistInDatabaseDebugger extends Debugger {
    public TrackingExistInDatabaseDebugger() {
        codeErr = ErrCodeList.trackingExistInDatabase;
    }

    @Override
    public boolean hasErr(DeviceTracking tracking) {
        return Tools.trackingDAO.ifExist(tracking.getId_device(), tracking.getId_vehicule(), tracking.getTracking_time());
    }
}

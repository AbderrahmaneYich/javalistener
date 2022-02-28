package app.tools.trackingDebugger.debuggers;

import app.model.DeviceTracking;
import app.tools.trackingDebugger.helpers.Debugger;
import app.tools.trackingDebugger.helpers.ErrCodeList;

//import app.tools.Console;

public class FixBrokenTrackingData extends Debugger {
    public FixBrokenTrackingData() {
        codeErr = ErrCodeList.trackingWithBrokenImportantData;
    }

    @Override
    public boolean hasErr(DeviceTracking tracking) {
        if (tracking.getPrevious() != null) {
            if (tracking.LVCAN_FUEL_LEVEL == 0 && tracking.getPrevious().LVCAN_FUEL_LEVEL > 0) {
                tracking.LVCAN_FUEL_LEVEL = tracking.getPrevious().LVCAN_FUEL_LEVEL;
            }
            if (tracking.LVCAN_FUEL_CONSUMED == 0 && tracking.getPrevious().LVCAN_FUEL_CONSUMED > 0) {
                tracking.LVCAN_FUEL_CONSUMED = tracking.getPrevious().LVCAN_FUEL_CONSUMED;
            }
            if (tracking.LVCAN_TOTAL_MILEAGE == 0 && tracking.getPrevious().LVCAN_TOTAL_MILEAGE > 0) {
                tracking.LVCAN_TOTAL_MILEAGE = tracking.getPrevious().LVCAN_TOTAL_MILEAGE;
            }
            if ((tracking.getReservoir1() == null || tracking.getReservoir1() == 0 )&& tracking.getPrevious().getReservoir1() != null && tracking.getPrevious().getReservoir1() > 0) {
                tracking.setReservoir1(tracking.getPrevious().getReservoir1());
            }
        }
        return false;
    }
}

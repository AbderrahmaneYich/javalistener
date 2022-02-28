package app.tools.trackingDebugger.debuggers;

import app.model.DeviceTracking;
import app.tools.trackingDebugger.helpers.Debugger;
import app.tools.trackingDebugger.helpers.ErrCodeList;

//import app.tools.Console;

public class TrackingWithBrokenImportantData extends Debugger {
    public TrackingWithBrokenImportantData() {
        codeErr = ErrCodeList.trackingWithBrokenImportantData;
    }

    @Override
    public boolean hasErr(DeviceTracking tracking) {
//        if (tracking.getLatitude() > 90 || tracking.getLatitude() < -90) {
//            Console.println("Latitude Err:" + tracking.getLatitude());
//            return true;
//        } else if (tracking.getLongitude() > 180 || tracking.getLongitude() < -180) {
//            Console.println("Longitude Err:" + tracking.getLongitude());
//            return true;
//        } else
//        if (tracking.getAltitude() == null) {
//            Console.println("Altitude Err:" + tracking.getAltitude());
//           Console.println(tracking.toString());
//            return true;
//        }
        return false;
    }
}

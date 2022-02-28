package app.tools.trackingDebugger.debuggers;

import app.model.DeviceTracking;
import app.tools.Tools;
import app.tools.trackingDebugger.helpers.Debugger;
import app.tools.trackingDebugger.helpers.ErrCodeList;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TrackingIsOldDebugger extends Debugger {
    public TrackingIsOldDebugger() {
        codeErr = ErrCodeList.trackingIsOld;
    }

    @Override
    public boolean hasErr(DeviceTracking tracking) {
        Date tracking_date = Tools.DateTime.stringToDate(tracking.getTracking_time());
        if (tracking_date == null)
            return true;
        Date date_now = new Date();
        LocalDateTime local_tracking_date = Tools.DateTime.dateToLocalDateTime(tracking_date);
        LocalDateTime local_date_now = Tools.DateTime.dateToLocalDateTime(date_now);
//        Console.println(local_tracking_date.toString());
        return ChronoUnit.DAYS.between(local_tracking_date, local_date_now) > 30;
    }
}

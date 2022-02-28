package app.tools.trackingDebugger.debuggers;

import app.dto.MailOverTimeDTO;
import app.model.DeviceTracking;
import app.tools.Tools;
import app.tools.trackingDebugger.helpers.Debugger;
import app.tools.trackingDebugger.helpers.ErrCodeList;

import java.util.Date;

public class SuspectTimeDebugger extends Debugger {
    private final long isSuspect_time;

    public SuspectTimeDebugger(long isSuspect_time) {
        codeErr = ErrCodeList.isSuspectTime;
        this.isSuspect_time = isSuspect_time;
    }

    @Override
    public boolean hasErr(DeviceTracking tracking) {
        Date date_now = new Date();
        Date tracking_date = Tools.DateTime.stringToDate(tracking.getTracking_time());
        long diffDate = date_now.getTime() - tracking_date.getTime();
        if (diffDate < -isSuspect_time) {
            int isSuspect = Tools.trackingDAO.checkSuspect(tracking.getId_device(), tracking.getId_vehicule());
            if (isSuspect <= 4) {
                if (isSuspect == 0) {
                    Tools.trackingDAO.addSuspect(tracking.getId_device(), tracking.getId_vehicule());
                } else {
                    Tools.trackingDAO.updateSuspect(tracking.getId_device(), tracking.getId_vehicule(), isSuspect + 1);
                }
            } else {
                if (isSuspect == 5 || (isSuspect + 1) % 50 == 0) {
                    MailOverTimeDTO mailOverTimeDTO = Tools.trackingDAO.getInfo(tracking.getId_device(), tracking.getId_vehicule());
                    mailOverTimeDTO.setNbreTracking(isSuspect + 1);
                    Tools.sendMail(mailOverTimeDTO);
                }
                Tools.trackingDAO.updateSuspect(tracking.getId_device(), tracking.getId_vehicule(), isSuspect + 1);
            }
            return true;
        }
        return false;
    }
}

package app.tools;

import app.dto.TrackingDTO;
import app.dto.TrackingDailyDTO;
import app.model.DeviceTracking;
import app.model.Tracking;
import config.Env;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class SenderCalculator implements Runnable {

    private TrackingDTO dto;
    private Tracking tracking;
    private boolean today = false;

    private SenderCalculator(TrackingDTO dto) {
        this.dto = dto;
    }

    private SenderCalculator(Tracking tracking, boolean today) {
        // TODO Auto-generated constructor stub
        this.tracking = tracking;
        this.today = today;
    }

    public static void checkAndHandler(Date tracking_date, long secTime, DeviceTracking tracking) {
        LocalDate localDateNow = LocalDate.now();
        LocalDate local_date_tracking = tracking_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Thread thread = null;
        if (local_date_tracking.isBefore(localDateNow) && (secTime > Tools.DateTime.T.h)) {
            thread = new Thread(new SenderCalculator(tracking.toDTO()));
        } else if (local_date_tracking.isEqual(localDateNow) && (secTime > 40 * Tools.DateTime.T.m)) {
            //   thread = new Thread(new SenderCalculator(tracking, true));
        }
        if (thread == null)
            return;
        thread.setName("SenderCalculator");
        thread.start();
    }

    @Override
    public void run() {
        try {
            if (today) {
                Tracking nextTracking = Tools.trackingDAO.getNext(tracking.getId_device(), tracking.getId_vehicule(), tracking.getTracking_time());
                if (nextTracking != null) {
                    int diffKm = (int) Tools.distanceBetween(tracking.getLatitude(), tracking.getLongitude(), nextTracking.getLatitude(), nextTracking.getLongitude());
                    TrackingDailyDTO dailyDTO = nextTracking.toDailyDTO(diffKm);
                    if (Env.Services.calculatorService != "disabled")
                        Tools.webResourcePost(Env.Services.calculatorService + "/km/calculDaily", Tools.objectToJsonToDay(dailyDTO));
                }
            } else {
                if (Env.Services.calculatorService != "disabled")
                    Tools.webResourcePost(Env.Services.calculatorService + "/km/calcul", Tools.objectToJson(this.dto));
            }

        } catch (Exception e) {
            Console.printStackTrace(e);
        }

    }

}

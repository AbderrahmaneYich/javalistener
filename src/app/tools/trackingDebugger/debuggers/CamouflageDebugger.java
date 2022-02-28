package app.tools.trackingDebugger.debuggers;

import app.model.DeviceTracking;
import app.tools.Console;
import app.tools.Tools;
import app.tools.trackingDebugger.helpers.CamouflagedMessage;
import app.tools.trackingDebugger.helpers.CamouflagedMessageService;
import app.tools.trackingDebugger.helpers.Debugger;
import config.ListDevices;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class CamouflageDebugger extends Debugger {
    private final boolean temperatureHandler;
    private final boolean minSpeedHandler;
    private final boolean distanceLogiqueHandler;

    public CamouflageDebugger(boolean handelTemperature) {
        this.temperatureHandler = handelTemperature;
        minSpeedHandler = false;
        distanceLogiqueHandler = false;
    }

    private static boolean speedLessThanOrEqualsAndAccInactive(DeviceTracking tracking, int minAcceptedSpeed) {
        return tracking.getAcc_status() == 0 && tracking.getSpeed() <= minAcceptedSpeed;
    }

    @Override
    public DeviceTracking debugging(DeviceTracking tracking) {
        try {
            // Correcting Data  km , speed , camStar
            boolean needCorrectGpsData = false;
            DeviceTracking previous = tracking.getPrevious();
            if (previous != null) {
                if (tracking.getKm() == null || tracking.deviceDetails.setKmUsingDistanceBetweenPrevious) {
                    tracking.setKmUsingDistanceBetweenPrevious();
                }
                tracking.setSpeed(Tools.logiqueSpeeding(tracking, previous));//this function use the  gps_speed and speed

                if (minSpeedHandler && tracking.getKm() < 10) {
                    needCorrectGpsData = true;
                    if (camStar(5, tracking)) {
                        tracking.setMessage(CamouflagedMessageService.createMessage(tracking, "camStar", 0));
                    } else {
                        tracking.setMessage(CamouflagedMessageService.createMessage(tracking, "10_m", 0));
                    }
                }
                if (!needCorrectGpsData && distanceLogiqueHandler) {
                    if (!Tools.isDistanceLogique(tracking.getKm(), tracking, previous)) {
                        int compteur = CamouflagedMessageService.compteur(previous);
                        if (compteur < 2) {
                            compteur++;
                            tracking.setMessage(CamouflagedMessageService.createMessage(tracking, "isDistanceLogique", compteur));
                            needCorrectGpsData = true;
                        } else {
                            CamouflagedMessage camMessage = CamouflagedMessageService.parseMessage(previous);
                            double distanceC = Tools.distanceBetween(tracking.getLatitude(), tracking.getLongitude(), camMessage.getLatitude(), camMessage.getLongitude());
                            if (!Tools.isDistanceLogique(distanceC, tracking, previous)) {
                                compteur++;
                                tracking.setMessage(CamouflagedMessageService.createMessage(tracking, "isDistanceLogique", compteur));
                                needCorrectGpsData = true;
                            }
                        }
                    }
                }
            }
            if (needCorrectGpsData) {
                tracking.setGps_speed(tracking.getSpeed().intValue());//todo??
                tracking.setSpeed(0);
                tracking.setAltitude(previous.getAltitude());
                tracking.setLatitude(previous.getLatitude());
                tracking.setLongitude(previous.getLongitude());
            }
            if (previous == null || needCorrectGpsData) {
                if (tracking.deviceDetails != ListDevices.teltonika3)
                    tracking.setKm((double) 0);
            }


//            ////////////////////////////////////////////////////////////////////////
//            handelTemperature
            if (tracking.getTemperature() != null) {
                if (tracking.getTemperature() == 85) {
                    if (previous == null)
                        tracking.setTemperature(0);
                    else
                        tracking.setTemperature(previous.getTemperature());
                } else {
                    if (temperatureHandler) {
                        if (tracking.getTemperature() >= 6) {
                            LocalTime local_tracking_time = Tools.DateTime.stringToLocalDate(tracking.getTracking_time()).toLocalTime();
                            if (local_tracking_time.isAfter(LocalTime.parse("04:00:00")) && local_tracking_time.isBefore(LocalTime.parse("04:30:00"))) {
                                double diff_min = ChronoUnit.MINUTES.between(LocalTime.parse("04:00:00"), local_tracking_time);
                                double condt = tracking.getTemperature() - diff_min * 1.5;
                                if (condt > 7.5) {
                                    tracking.setTemperature(condt);
                                } else {
                                    tracking.setTemperature(7 - 20 / tracking.getTemperature());
                                }
                            } else if (local_tracking_time.isAfter(LocalTime.parse("04:30:00")) && local_tracking_time.isBefore(LocalTime.parse("12:00:00"))) {
                                tracking.setTemperature(7 - 20 / tracking.getTemperature());
                            } else if (local_tracking_time.isAfter(LocalTime.parse("12:00:00")) && local_tracking_time.isBefore(LocalTime.parse("15:00:00"))) {
                                double diff_min = ChronoUnit.MINUTES.between(LocalTime.parse("12:00:00"), local_tracking_time);
                                double condt = tracking.getTemperature() - (7 - 20 / tracking.getTemperature() + diff_min * 0.2);
                                if (condt > 0) {
                                    tracking.setTemperature((7 - 20 / tracking.getTemperature()) + diff_min * 0.2);
                                }
                            }
                            BigDecimal bd = BigDecimal.valueOf(tracking.getTemperature());
                            bd = bd.setScale(2, BigDecimal.ROUND_DOWN);
                            tracking.setTemperature(bd.doubleValue());
                        }
                    }
                }
            }
            return tracking;
        } catch (Exception e) {
            Console.printStackTrace(e);
            return tracking;
        }
    }

    private boolean camStar(int minAcceptedSpeed, DeviceTracking tracking) {
        return speedLessThanOrEqualsAndAccInactive(tracking, minAcceptedSpeed) && speedLessThanOrEqualsAndAccInactive(tracking.getPrevious(), minAcceptedSpeed);
    }


}

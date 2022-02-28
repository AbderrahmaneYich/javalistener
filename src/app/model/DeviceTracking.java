package app.model;

import app.dto.TrackingDTO;
import app.tools.Tools;
import config.DeviceConfigs;

import java.util.ArrayList;

public class DeviceTracking extends Tracking implements Comparable<DeviceTracking> {
    public DeviceConfigs deviceDetails;
    public ArrayList<String> errCodes = new ArrayList<>();
    private DeviceTracking previous;


    public DeviceTracking() {
        super();
    }

    public TrackingDTO toDTO() {
        return new TrackingDTO(this.getId_vehicule(), this.getKm(), this.getTracking_time(), this.getLatitude(), this.getLongitude());
    }
//
//    public boolean deviceSpeedLessThanOrEqualsAndAccInactive(int speed) {
//        return this.getAcc_status() == 0 && this.getSpeed() <= speed;
//    }
//
//    public LocalDateTime getLocalTrackingTime() {
//        return Tools.DateTime.stringToLocalDate(this.getTracking_time());
//    }

    public DeviceTracking loadPrevious() {
        previous = Tools.trackingDAO.getPrevious(this.getId_device(), this.getId_vehicule(), this.getTracking_time());
        return previous;
    }

    public DeviceTracking getPrevious() {
        if (previous == null)
            return loadPrevious();
        return previous;
    }


    public void setKmUsingDistanceBetweenPrevious() {
        if (!this.HavOdometer && getPrevious() != null)
            setKm(Tools.distanceBetween(getPrevious().getLatitude(), getPrevious().getLongitude(), this.getLatitude(), this.getLongitude()));// meters
    }


    @Override
    public int compareTo(DeviceTracking tracking) {
        if (getTracking_time() == null || tracking.getTracking_time() == null) {
            return 0;
        }
        return getTracking_time().compareTo(tracking.getTracking_time());
    }
}

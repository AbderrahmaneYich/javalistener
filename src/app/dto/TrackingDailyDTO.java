package app.dto;

public class TrackingDailyDTO {


    private Integer idVehicle;

    private Double km;

    private String trackingTime;

    private Integer idTracking;

    private Integer diffKm;

    private Integer idDevice;


    public TrackingDailyDTO(Integer idVehicle, Double km, String trackingTime, Integer idTracking, Integer diffKm,
                            Integer idDevice) {
        super();
        this.idVehicle = idVehicle;
        this.km = km;
        this.trackingTime = trackingTime;
        this.idTracking = idTracking;
        this.diffKm = diffKm;
        this.idDevice = idDevice;
    }


    public Integer getIdVehicle() {
        return idVehicle;
    }


    public Double getKm() {
        return km;
    }


    public String getTrackingTime() {
        return trackingTime;
    }


    public Integer getIdTracking() {
        return idTracking;
    }


    public Integer getDiffKm() {
        return diffKm;
    }


    public Integer getIdDevice() {
        return idDevice;
    }


    @Override
    public String toString() {
        return "TrackingDailyDTO [idVehicle=" + idVehicle + ", km=" + km + ", trackingTime=" + trackingTime
                + ", idTracking=" + idTracking + ", diffKm=" + diffKm + ", idDevice=" + idDevice + "]";
    }


}

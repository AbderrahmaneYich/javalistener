package app.dto;

public class TrackingDTO {

    private Integer idVehicle;
    private Double km;
    private String trackingTime;
    private Double latitude;
    private Double longitude;

    public TrackingDTO(Integer idVehicle, Double km, String trackingTime, Double latitude, Double longitude) {
        this.idVehicle = idVehicle;
        this.km = km;
        this.trackingTime = trackingTime;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }


}

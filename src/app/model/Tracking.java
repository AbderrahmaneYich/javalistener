package app.model;

import app.dto.TrackingDTO;
import app.dto.TrackingDailyDTO;
import com.google.gson.JsonObject;


public class Tracking {
    public int LVCAN_ENGINE_WORKTIME;
    public long id;
    //    public int port;
    public double LVCAN_VEHICLE_SPEED;
    public double LVCAN_ACCELERATOR_PEDAL_POSITION;
    public double LVCAN_FUEL_CONSUMED;
    public double LVCAN_FUEL_LEVEL;
    public int LVCAN_ENGINE_RPM;
    public double LVCAN_TOTAL_MILEAGE;
    public int LVCAN_DOOR_STATUS;
    public double LVCAN_FUEL_RATE;
    public double LVCAN_ENGINE_TEMPERATURE;
    public double ENGINE_OIL_LEVEL;
    public double GREEN_DRIVING_TYPE;
    public double CRASH_DETECTION;
    public double GREEN_DRIVING_VALUE;
    public double LVCAN_JAMMING;
    public double GREEN_DRIVING_EVENT_DURATION;
    public JsonObject avlData = new JsonObject();

    //    private ArrayList<AvlDataElement> avlDataElements = new ArrayList<>();
    public boolean alert_checked;
    public boolean distance_corrected;
    protected Double longitude;
    protected Double latitude;
    protected String message;
    protected Double speed;
    protected Double km;
    private Integer id_tracking;
    private String imei;
    private String tracking_time;
    private Double altitude;
    private Double angle;
    private int acc_status = 0;
    private Integer gps_speed;
    private Integer etatSignal;
    private Integer satellites;
    private Double reservoir1;
    private Double reservoir2;
    private Double temperature;
    private Double temperature2;
    private Integer id_device;
    private Integer id_vehicule;
    private String input;
    private Double gps_hdop;
    private Double gps_pdop;
    private Double internal_battery;
    private Double external_power;
    private Double current_battery;
    private Integer priority;
    private Integer tracking_on_event;
    private Integer event;
    private Integer panic_button_status;
    private Integer etat;
    private Integer movement;
    private Integer actual_profile;
    private Double pcb_temp;
    private Integer sleep_mode;
    private Integer operator_code;
    private Integer area_code;
    private Integer cell_id;
    private Integer gnss_status;
    private String rfid;

    public Boolean HavOdometer=false;

    public Tracking() {
    }


    private Integer getId_tracking() {
        return id_tracking;
    }

    public void setId_tracking(Integer id_tracking) {
        this.id_tracking = id_tracking;
    }


    public String getTracking_time() {
        return tracking_time;
    }

    public void setTracking_time(String tracking_time) {
        this.tracking_time = tracking_time;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public Double getAngle() {
        return angle;
    }

    private void setAngle(Double angle) {
        this.angle = angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public int getAcc_status() {
        return acc_status;
    }

    public void setAcc_status(int acc_status) {
        this.acc_status = acc_status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Integer getGps_speed() {
        return gps_speed;
    }

    public void setGps_speed(Integer gps_speed) {
        this.gps_speed = gps_speed;
    }

    public Integer getEtatSignal() {
        return etatSignal;
    }

//    public void setEtatSignal(Integer etatSignal) {
//        this.etatSignal = etatSignal;
//    }

    public void setEtatSignal(int etatSignal) {
        this.etatSignal = etatSignal;
    }

    public Integer getSatellites() {
        return satellites;
    }

    private void setSatellites(Integer satellites) {
        this.satellites = satellites;
    }

    public void setSatellites(int satellites) {
        this.satellites = satellites;
    }

    public Double getReservoir1() {
        return reservoir1;
    }

//    public void setReservoir1(Double reservoir1) {
//        this.reservoir1 = reservoir1;
//    }

    public void setReservoir1(double reservoir1) {
        this.reservoir1 = reservoir1;
    }

    public Double getReservoir2() {
        return reservoir2;
    }

//    public void setReservoir2(Double reservoir2) {
//        this.reservoir2 = reservoir2;
//    }

    public void setReservoir2(double reservoir2) {
        this.reservoir2 = reservoir2;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public Double getTemperature2() {
        return temperature2;
    }

    public void setTemperature2(Double temperature2) {
        this.temperature2 = temperature2;
    }

    public void setTemperature2(double temperature2) {
        this.temperature2 = temperature2;
    }

    public Double getKm() {
        return km;
    }

    public void setKm(Double km) {
        this.km = km;
    }

    public Integer getId_device() {
        return id_device;
    }

//    public void setId_device(Integer id_device) {
//        this.id_device = id_device;
//    }

    public void setId_device(int id_device) {
        this.id_device = id_device;
    }

    public Integer getId_vehicule() {
        return id_vehicule;
    }

//    public void setId_vehicule(Integer id_vehicule) {
//        this.id_vehicule = id_vehicule;
//    }

    public void setId_vehicule(int id_vehicule) {
        this.id_vehicule = id_vehicule;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Double getGps_hdop() {
        return gps_hdop;
    }

//    public void setGps_hdop(Double gps_hdop) {
//        this.gps_hdop = gps_hdop;
//    }

    public void setGps_hdop(double gps_hdop) {
        this.gps_hdop = gps_hdop;
    }

    public Double getGps_pdop() {
        return gps_pdop;
    }

//    public void setGps_pdop(Double gps_pdop) {
//        this.gps_pdop = gps_pdop;
//    }
//
//    public void setGps_pdop(double gps_pdop) {
//        this.gps_pdop = gps_pdop;
//    }

    public Double getInternal_battery() {
        return internal_battery;
    }

//    public void setInternal_battery(Double internal_battery) {
//        this.internal_battery = internal_battery;
//    }

    public void setInternal_battery(double internal_battery) {
        this.internal_battery = internal_battery;
    }

    public Double getExternal_power() {
        return external_power;
    }

//    public void setExternal_power(Double external_power) {
//        this.external_power = external_power;
//    }

    public void setExternal_power(double external_power) {
        this.external_power = external_power;
    }

    public Double getCurrent_battery() {
        return current_battery;
    }

//    public void setCurrent_battery(Double current_battery) {
//        this.current_battery = current_battery;
//    }

    public void setCurrent_battery(double current_battery) {
        this.current_battery = current_battery;
    }

    public Integer getPriority() {
        return priority;
    }

    private void setPriority(Integer priority) {
        this.priority = priority;
    }

//    public void setPriority(int priority) {
//        this.priority = priority;
//    }

    public Integer getTracking_on_event() {
        return tracking_on_event;
    }

//    public void setTracking_on_event(Integer tracking_on_event) {
//        this.tracking_on_event = tracking_on_event;
//    }
//
//    public void setTracking_on_event(int tracking_on_event) {
//        this.tracking_on_event = tracking_on_event;
//    }

    public Integer getEvent() {
        return event;
    }

//    public void setEvent(Integer event) {
//        this.event = event;
//    }
//
//    public void setEvent(int event) {
//        this.event = event;
//    }

    public Integer getPanic_button_status() {
        return panic_button_status;
    }

//    public void setPanic_button_status(Integer panic_button_status) {
//        this.panic_button_status = panic_button_status;
//    }
//
//    public void setPanic_button_status(int panic_button_status) {
//        this.panic_button_status = panic_button_status;
//    }

    public Integer getEtat() {
        return etat;
    }

    //    public void setEtat(Integer etat) {
//        this.etat = etat;
//    }
//
//    public void setEtat(int etat) {
//        this.etat = etat;
//    }
//
    public Integer getMovement() {
        return movement;
    }

//    public void setMovement(Integer movement) {
//        this.movement = movement;
//    }

    public void setMovement(int movement) {
        this.movement = movement;
    }

    public Integer getActual_profile() {
        return actual_profile;
    }

//    public void setActual_profile(Integer actual_profile) {
//        this.actual_profile = actual_profile;
//    }

    public void setActual_profile(int actual_profile) {
        this.actual_profile = actual_profile;
    }

    public Double getPcb_temp() {
        return pcb_temp;
    }

//    public void setPcb_temp(Double pcb_temp) {
//        this.pcb_temp = pcb_temp;
//    }

    public void setPcb_temp(double pcb_temp) {
        this.pcb_temp = pcb_temp;
    }

    public Integer getSleep_mode() {
        return sleep_mode;
    }

//    public void setSleep_mode(Integer sleep_mode) {
//        this.sleep_mode = sleep_mode;
//    }

    public void setSleep_mode(int sleep_mode) {
        this.sleep_mode = sleep_mode;
    }

    public Integer getOperator_code() {
        return operator_code;
    }

//    public void setOperator_code(Integer operator_code) {
//        this.operator_code = operator_code;
//    }

    public void setOperator_code(int operator_code) {
        this.operator_code = operator_code;
    }

    public Integer getArea_code() {
        return area_code;
    }

//    public void setArea_code(Integer area_code) {
//        this.area_code = area_code;
//    }

    public void setArea_code(int area_code) {
        this.area_code = area_code;
    }

    public Integer getCell_id() {
        return cell_id;
    }

    //    public void setCell_id(Integer cell_id) {
//        this.cell_id = cell_id;
//    }
//
//    public void setCell_id(int cell_id) {
//        this.cell_id = cell_id;
//    }
//
    public Integer getGnss_status() {
        return gnss_status;
    }

//    public void setGnss_status(Integer gnss_status) {
//        this.gnss_status = gnss_status;
//    }

    public void setGnss_status(int gnss_status) {
        this.gnss_status = gnss_status;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    @Override
    public String toString() {
        return "Tracking [imei=" + imei + ", tracking_time=" + tracking_time + ", longitude=" + longitude
                + ", latitude=" + latitude + ", altitude=" + altitude + ", angle=" + angle + ", acc_status="
                + acc_status + ", message=" + message + ", speed=" + speed + ", gps_speed=" + gps_speed
                + ", etatSignal=" + etatSignal + ", satellites=" + satellites + ", reservoir1=" + reservoir1
                + ", reservoir2=" + reservoir2 + ", temperature=" + temperature + ", temperature2=" + temperature2
                + ", km=" + km + ", id_device=" + id_device + ", id_vehicule=" + id_vehicule + ", input=" + input
                + ", gps_hdop=" + gps_hdop + ", gps_pdop=" + gps_pdop + ", internal_battery=" + internal_battery
                + ", external_power=" + external_power + ", current_battery=" + current_battery + ", priority="
                + priority + ", tracking_on_event=" + tracking_on_event + ", event=" + event + ", panic_button_status="
                + panic_button_status + ", etat=" + etat + ", movement=" + movement + ", actual_profile="
                + actual_profile + ", pcb_temp=" + pcb_temp + ", sleep_mode=" + sleep_mode + ", operator_code="
                + operator_code + ", area_code=" + area_code + ", cell_id=" + cell_id + ", gnss_status=" + gnss_status
                + ", rfid=" + rfid + "]";
    }

    public TrackingDTO toDTO() {
        return new TrackingDTO(this.getId_vehicule(), this.getKm(), this.getTracking_time(),
                this.getLatitude(), this.getLongitude());
    }

    public TrackingDailyDTO toDailyDTO(int diffKm) {

        return new TrackingDailyDTO(this.getId_vehicule(), this.getKm(), this.getTracking_time(),
                this.getId_tracking(), diffKm, this.getId_device());
    }

    public void setBasicData(String imei, int id_device, int id_vehicule, Integer priority) {
        this.setImei(imei);
        this.setId_device(id_device);
        this.setId_vehicule(id_vehicule);
        this.setPriority(priority);
    }

    public void setGPSTrackingData(Double longitude, Double latitude, Double speed, Double angle, double altitude, Integer satellites) {
        this.setPriority(priority);
        this.setLongitude(longitude);
        this.setLatitude(latitude);
        this.setSpeed(speed);
        this.setAngle(angle);
        this.setAltitude(altitude);
        this.setSatellites(satellites);
    }

    public void setAvlData(String key, String val) {
        this.avlData.addProperty(key, val);
    }

    public void setAvlData(Integer key, String val) {
        this.setAvlData(String.valueOf(key), val);
    }

    public void setAvlData(Integer key, Double val) {
        this.setAvlData(String.valueOf(key), String.valueOf(val));
    }

    public void setAvlData(Integer key, Long val) {
        this.setAvlData(String.valueOf(key), String.valueOf(val));
    }

}
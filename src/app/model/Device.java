package app.model;

public class Device {
    public float volReservoir1Max;
    public float volReservoir1Min;
    public float volReservoir2Max;
    public float volReservoir2Min;
    private int id;
    private String imei;
    private int id_vehicule;

    public Device(int id, String imei, int id_vehicule) {
        this.id = id;
        this.imei = imei;
        this.id_vehicule = id_vehicule;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public int getId_vehicule() {
        return id_vehicule;
    }

//    public void setId_vehicule(int id_vehicule) {
//        this.id_vehicule = id_vehicule;
//    }


}

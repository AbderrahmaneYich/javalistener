package app.dto;

public class MailOverTimeDTO {

    private String imei;
    private String marque;
    private String matricule;
    private int nbreTracking;
    private String societe;
    private String gsm;


    public MailOverTimeDTO() {
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public int getNbreTracking() {
        return nbreTracking;
    }

    public void setNbreTracking(int nbreTracking) {
        this.nbreTracking = nbreTracking;
    }

    public String getSociete() {
        return societe;
    }

    public void setSociete(String societe) {
        this.societe = societe;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    @Override
    public String toString() {
        return "MailOverTimeDTO [imei=" + imei + ", marque=" + marque + ", matricule=" + matricule + ", nbreTracking="
                + nbreTracking + ", societe=" + societe + ", gsm=" + gsm + "]";
    }


}

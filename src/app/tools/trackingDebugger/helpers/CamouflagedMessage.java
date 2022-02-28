package app.tools.trackingDebugger.helpers;

import app.model.Tracking;

public class CamouflagedMessage extends Tracking {

    private String cause;
    private int compteur;


    public int getCompteur() {
        return compteur;
    }

    public void setCompteur(int compteur) {
        this.compteur = compteur;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

}

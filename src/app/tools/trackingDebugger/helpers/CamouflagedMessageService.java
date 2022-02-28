package app.tools.trackingDebugger.helpers;

import app.model.Tracking;

public class CamouflagedMessageService {

    public static String createMessage(Tracking tracking, String cause, int compteur) {
        return tracking.getLatitude() + ";"
                + tracking.getLongitude() + ";"
                + tracking.getSpeed() + ";" + tracking.getKm() + ";" + compteur + ";"
                + tracking.getAltitude() + ";" + cause;


    }


    public static CamouflagedMessage parseMessage(Tracking tracking) {

        CamouflagedMessage camouflagedMessage = new CamouflagedMessage();


        if (tracking.getMessage() != null) {

            String[] splitedArray = tracking.getMessage().split(";");

            if (splitedArray.length >= 6) {

                camouflagedMessage.setLatitude(Double.valueOf(splitedArray[0]));

                camouflagedMessage.setLongitude(Double.valueOf(splitedArray[1]));

                camouflagedMessage.setSpeed(Double.valueOf(splitedArray[2]));

                camouflagedMessage.setKm(Double.valueOf(splitedArray[3]));

                camouflagedMessage.setCompteur(Integer.parseInt(splitedArray[4]));

                camouflagedMessage.setAltitude(Double.valueOf(splitedArray[5]));

                if (splitedArray.length >= 7) camouflagedMessage.setCause(splitedArray[6]);

            }

        }

        return camouflagedMessage;
    }


    public static int compteur(Tracking tracking) {

        int cmp = 0;

        if (tracking.getMessage() != null) {

            String[] splitedArray = tracking.getMessage().split(";");

            if (splitedArray.length >= 4) {

                cmp = Integer.valueOf(splitedArray[4]);

            }


        }
        if (cmp >= 0)
            return cmp;
        else
            return 0;
    }


}

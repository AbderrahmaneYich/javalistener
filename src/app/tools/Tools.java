package app.tools;

import app.dao.DeviceDAO;
import app.dao.TrackingDAO;
import app.dto.MailOverTimeDTO;
import app.dto.TrackingDTO;
import app.dto.TrackingDailyDTO;
import app.model.DeviceTracking;
import app.model.Tracking;
import app.tools.trackingDebugger.helpers.CamouflagedMessageService;
import app.tools.vondors.LatLng;
import app.tools.vondors.SphericalUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import config.Env;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Tools {
    public static TrackingDAO trackingDAO;
    public static DeviceDAO deviceDAO;

    public static String objectToJson(TrackingDTO tracking) {
        String objectString = "{";
        objectString += " \"trackingTime\" : \"" + tracking.getTrackingTime() + "\",";
        objectString += " \"longitude\" : " + tracking.getLongitude() + ",";
        objectString += " \"latitude\" : " + tracking.getLatitude() + ",";
        objectString += " \"km\" : " + tracking.getKm() + ",";
        objectString += " \"idVehicle\" : " + tracking.getIdVehicle();
        objectString += "}";

        return objectString;
    }

    public static void webResourcePost(String resource, String input) {
        Client client = Client.create();
        try {
            WebResource webResource = client.resource(resource);
            client.setConnectTimeout(10000);
            client.setReadTimeout(10000);
//            Console.println(resource);
//            Console.println(input);
            webResource.type("application/json").post(ClientResponse.class, input);
//            Console.println("ok");
        } catch (Exception e) {
            if (!Console.Mute.getWebResourcePost())
                Console.errPrintln("ERROR Communication with " + resource + " : " + e.getMessage());
        } finally {
            client.destroy();
        }
    }

    public static String objectToJsonToDay(TrackingDailyDTO tracking) {
        String objectString = "{";
        objectString += " \"trackingTime\" : \"" + tracking.getTrackingTime() + "\",";
        objectString += " \"correctedKm\" : " + tracking.getDiffKm() + ",";
        objectString += " \"idTracking\" : " + tracking.getIdTracking() + ",";
        objectString += " \"km\" : " + tracking.getKm() + ",";
        objectString += " \"idDevice\" : " + tracking.getIdDevice() + ",";
        objectString += " \"idVehicle\" : " + tracking.getIdVehicle();
        objectString += "}";

        return objectString;
    }

    public static boolean isNumeric(String s) {
        try {
            return s.matches("[0-9]*");
        } catch (NumberFormatException e) {
//            Console.printStackTrace(e);
        }
        return false;
    }

    public static String splitPutsM(String tram) {
        StringBuilder tramBuilder = new StringBuilder(tram);
        while (tramBuilder.length() < 16) {
            tramBuilder.insert(0, "0");
        }
        return tramBuilder.toString();
    }


    static String convertType(Tracking current) {
        return "{" + "\"dateTracking\" : \" " + current.getTracking_time() + "\"," + "\"longitude\" : \""
                + current.getLongitude() + "\"," + "\"latitude\" : \"" + current.getLatitude() + "\","
                + "\"altitude\" : \"" + current.getAltitude() + "\"," + "\"acc\" : \"" + current.getAcc_status() + "\","
                + "\"message\" : \"" + current.getMessage() + "\"," + "\"speed\" : \"" + current.getSpeed() + "\","
                + "\"nombreSattelite\" : \"" + current.getSatellites() + "\"," + "\"kilometrage\" : \""
                + current.getKm() + "\"," + "\"idDevice\" : \"" + current.getId_device() + "\"," + "\"idVehicule\" : \""
                + current.getId_vehicule() + "\"," + "\"gpsSpeed\" : \"" + current.getGps_speed() + "\","
                + "\"input\" : \"" + current.getInput() + "\" " + "}";
    }

    public static double logiqueSpeeding(Tracking current, Tracking previous) {
        if (current.getSpeed() == -1) {
            if (previous != null) {
                if (lessThanPercent(current.getGps_speed(), previous.getSpeed(), 0.6)) {
                    return current.getGps_speed();
                } else {
                    return previous.getSpeed();
                }
            } else {
                return 0;
            }
        }
        return current.getSpeed();
    }

    private static boolean lessThanPercent(double speed1, double speed2, double percent) {
        if (speed2 > speed1) {
            return (speed2 * percent) < speed1;
        } else {
            return (speed1 * percent) < speed2;
        }
    }


    private static double getSpeedLogique(double dTime) {
        double speedLogique = ((double) 150 * 1000) / ((double) 60 * 60); // metre par seconde  150km/h
        return speedLogique * dTime;
    }

    public static boolean isDistanceLogique(double distance, DeviceTracking current, DeviceTracking previous) {
        Date dateCurrent = Tools.DateTime.stringToDate(current.getTracking_time());
        Date datePrevious = Tools.DateTime.stringToDate(previous.getTracking_time());
        double dTime = (dateCurrent.getTime() - datePrevious.getTime()) / DateTime.T.s; // valeur par seconde
        if (distance > getSpeedLogique(dTime) + 500) {
            return false;
        } else {
            if (current.getAltitude() != null)
                return isHDistanceLogique(current, previous);
            return true;
        }
    }

    private static boolean isHDistanceLogique(DeviceTracking current, DeviceTracking previous) {
        int compteur = CamouflagedMessageService.compteur(previous);
        double altitude = (compteur == 0) ? previous.getAltitude() : CamouflagedMessageService.parseMessage(previous).getAltitude();
        return (Math.abs(current.getAltitude() - altitude) < 1000);
    }

    public static double distanceBetween(double lat1, double lon1, double lat2, double lon2) {
        LatLng newT = new LatLng(lat1, lon1);
        LatLng previousT = new LatLng(lat2, lon2);
        return SphericalUtil.computeDistanceBetween(newT, previousT);
    }

    static void sendTracking(String tracking) {
        for (String alertService : Env.Services.alertServices) {
            webResourcePost(alertService, tracking);
        }
    }

    static public void sendMail(MailOverTimeDTO mailDTO) {
        if (mailDTO != null) {
            String message = generateMessage(mailDTO);
            sendMessage(message, "Anomalie TimeZone");
        }
    }

    private static String generateMessage(MailOverTimeDTO m) {
        String message;
        message = "<div style=\"font-size:1.4em;\"><img style=\"text-aligne:right;max-width:80px\" src=\"http://jptrack.jp.co.ma/img/logo.png\" alt=\"JPTRACK\">";
        message += "<h1 style=\"font-size:1.6em;color:#505050 ;font-family: sans-serif;\">Anomalie time zone</h1>";
        message += "<hr>";
        message += "<strong>Bonjour,</strong><br>";
        message += "Nous vous informons de l\'anomalie : <strong> TimeZone </strong>";
        if (m.getMatricule() != null) {
            if (m.getMatricule() != null)
                message += "<br>Matricule : <strong>" + m.getMatricule() + "</strong>";
            if (m.getGsm() != null)
                message += "<br>Numero d'appel: <strong>" + m.getGsm() + "</strong>";
            if (m.getImei() != null)
                message += "<br>Imei : <strong>" + m.getImei() + "</strong>";
            if (m.getMarque() != null)
                message += "<br>Marque de boitier: <strong>" + m.getMarque() + "</strong>";
            if (m.getSociete() != null)
                message += "<br>Societé: <strong>" + m.getSociete() + "</strong>";
            message += "<br>Nombre de tracking : <strong>" + m.getNbreTracking() + "</strong>";
        }
        return message;
    }

    public static void sendMessage(String msgMail, String subject) {
        try {
            Session session = Session.getInstance(Env.SMTP.props(), new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(Env.SMTP.username, Env.SMTP.password);
                }
            });
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(Env.SMTP.mail_from));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Env.SMTP.mails[0]));
                message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(Env.SMTP.mails[1]));
                message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(Env.SMTP.mails[3]));
                message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(Env.SMTP.mails[4]));
                message.setSubject(subject);
                message.setContent(msgMail, "text/html");
                Transport.send(message);
            } catch (MessagingException e) {
                Console.printStackTrace(e);
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            Console.printStackTrace(e);
            Console.println(" Erreur envoie : Resume error : " + e.toString(), Console.Colors.getRED());
        }
    }


    /////////////////////////////////////////////////////////////////////

    public static class DateTime {

        public static Date stringToDate(String datetime, String format) {
            try {
                return (new SimpleDateFormat(format)).parse(datetime);
            } catch (ParseException e) {
                Console.printStackTrace(e);
                return null;
            }
        }

        public static Date stringToDate(String datetime) {
            return stringToDate(datetime, "yyyy-MM-dd HH:mm:ss");
        }

        private static LocalDateTime stringToLocalDate(String datetime, String format) {
            Date date = stringToDate(datetime, format);
            if (date != null)
                return dateToLocalDateTime(date);
            return null;
        }

        public static LocalDateTime stringToLocalDate(String datetime) {
            return stringToLocalDate(datetime, "yyyy-MM-dd HH:mm:ss");
        }

        public static LocalDateTime dateToLocalDateTime(Date datetime) {
            return datetime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        public static String getTimestampFromDate(String date) {
            Date tempDate = stringToDate(date, "yyMMddHHmmss");
            assert tempDate != null;
            return new Timestamp(tempDate.getTime()).toString();

        }


        public static String dateNow(String format) {

            if (format == null)
                format = "yyyy-MM-dd HH:mm:ss";
            return DateTimeFormatter.ofPattern(format, Locale.ENGLISH).format(LocalDateTime.now());
        }

        public class T {
            public static final int ms = 1;
            public static final int s = 1000;
            public static final int m = 60 * s;
            public static final int h = 60 * m;
            public static final int d = 24 * h;
        }


    }


    public static class GBS {
        public static double toDD(String coords, String coords_position, int lngDegreeEnd) {
            double lngDegree = Integer.parseInt(coords.substring(0, lngDegreeEnd));
            double minutes = Double.parseDouble(coords.substring(lngDegreeEnd));
            char c = coords_position.charAt(0);
            double out = lngDegree + (minutes / 60);
            if (c == 'S' || c == 'W') {
                out = out * -1.0;
            }
            return out;
        }
    }
}

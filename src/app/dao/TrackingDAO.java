package app.dao;

import app.dto.MailOverTimeDTO;
import app.model.DeviceTracking;
import app.model.Tracking;
import app.tools.Console;
import app.tools.DBCommand;
import app.tools.Tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TrackingDAO {
    public boolean insertTracking(Tracking tracking, boolean withId, long diffTime) {

        DBCommand.DbInsert req = DBCommand.DbInsert.prepareQuery(tracking);
        String reqTracking = req.getSQL().replace("{dbtTableName}", "tracking");
        if (withId) {
            tracking.id = DBCommand.executeWithReturnId(reqTracking);
            //        reqTracking = req.add("ID_tracking", tracking.id).getSQL() ;
            //        int nbrDays = (int) (diffTime / Tools.DateTime.T.d);
            //        if (nbrDays <= 91) {
            //            DBCommand.execute(reqTracking.replace("{dbtTableName}", "last_tracking_tmonths"));
            //            if (nbrDays <= 3) {
            //                DBCommand.execute(reqTracking.replace("{dbtTableName}", "last_tracking_tdays"));
            //            }
            //        }
        } else {
            DBCommand.execute(reqTracking);
        }

        return true;
    }

    public DeviceTracking getPrevious(int idDevice, int idVehicule, String dateTracking) {
        try {
            LocalDateTime firstDate = Tools.DateTime.stringToLocalDate(dateTracking);
            LocalDateTime secondDate = Tools.DateTime.dateToLocalDateTime(new Date());
            long diffDate = ChronoUnit.DAYS.between(firstDate, secondDate);
            String table_name = "";
            if (diffDate < 3) {
                table_name = DbTables.tracking_three_days;
            } else if (diffDate < 30) {
                table_name = DbTables.tracking_three_months;
            }

            Statement statement = DBCommand.getDbStatement();
            ResultSet resultSet = DBCommand.executeSelectOneQuery(statement,
                    " SELECT tracking_time, longitude, latitude, speed, gps_speed, altitude, message, acc_status, temperature1 FROM " + table_name
                            + " WHERE id_device=" + idDevice + " AND id_vehicule=" + idVehicule + "  AND tracking_time <'"
                            + dateTracking + "' ORDER BY tracking_time DESC LIMIT 0,1");
            if (resultSet != null) {
                DeviceTracking tracking = new DeviceTracking();
                tracking.setTracking_time(resultSet.getString("tracking_time"));
                tracking.setLongitude(resultSet.getDouble("longitude"));
                tracking.setLatitude(resultSet.getDouble("latitude"));
                tracking.setSpeed(resultSet.getDouble("speed"));
                tracking.setGps_speed(resultSet.getInt("gps_speed"));
                tracking.setAltitude(resultSet.getInt("altitude"));
                tracking.setMessage(resultSet.getString("message"));
                tracking.setTemperature(resultSet.getInt("temperature1"));
                statement.close();
                resultSet.close();
                return tracking;
            }

        } catch (SQLException e) {
            Console.printStackTrace(e);
        }
        return null;
    }


    public boolean ifExist(int idDevice, int idVehicule, String dateTracking) {
        try {
            Date tracking_date = Tools.DateTime.stringToDate(dateTracking);
            Date now = new Date();
            long diffDate = (now.getTime() - tracking_date.getTime()) / Tools.DateTime.T.m;
            String table_name;
            if (diffDate < 3888) {
                table_name = DbTables.tracking_three_days;
            } else if (diffDate < 43200) {
                table_name = DbTables.tracking_three_months;
            } else {
                return true;
            }
            Statement statement = DBCommand.getDbStatement();
            ResultSet resultSet = DBCommand.executeSelectOneQuery(statement,
                    " SELECT id_tracking FROM " + table_name + " WHERE id_device=" + idDevice
                            + " AND id_vehicule=" + idVehicule + "  AND tracking_time ='" + dateTracking + "' LIMIT 0,1"
            );

            if (resultSet != null) {
                statement.close();
                resultSet.close();
                return true;
            }
        } catch (SQLException e1) {
            Console.printStackTrace(e1);
        }

        return false;
    }

    public int checkSuspect(int idDevice, int idVehicule) {
        try {
            Statement statement = DBCommand.getDbStatement();
            ResultSet resultSet = DBCommand.executeSelectOneQuery(statement,
                    " SELECT cmpt FROM tmp_over_time WHERE id_device=" + idDevice + " AND id_vehicule=" + idVehicule + " limit 0,1"
            );
            if (resultSet != null) {
                return resultSet.getInt("cmpt");
            }
        } catch (Exception e) {
            Console.printStackTrace(e);
        }

        return 0;
    }

    public void addSuspect(int idDevice, int idVehicle) {
        DBCommand.execute(" INSERT INTO tmp_over_time(id_device,id_vehicule,cmpt)" + " VALUES(" + idDevice + "," + idVehicle + ",1)"
        );
    }

    public void updateSuspect(int idDevice, int idVehicle, int cmpt) {
        DBCommand.execute(
                " UPDATE tmp_over_time SET id_device = " + idDevice + ",id_vehicule= " + idVehicle + ",cmpt = "
                        + cmpt + " WHERE id_device=" + idDevice + " AND id_vehicule=" + idVehicle
        );
    }


    public MailOverTimeDTO getInfo(int idDevice, int idVehicule) {
        // TODO Auto-generated method stub
        try {
            Statement statement = DBCommand.getDbStatement();
            String req = "select d.IMEI as imei,d.codec as marque, d.gsm as gsm, v.mat as matricule,"
                    + "s.raison_social as raison_social from device d " + "INNER JOIN vehicule v "
                    + "on d.id_vehicule = v.ID_Vehicule " + "inner join " + "societe s " + "on v.id_societe = s.id_societe "
                    + " WHERE d.ID_Device =" + idDevice + " AND v.ID_Vehicule=" + idVehicule + " limit 0,1";
            ResultSet resultSet = DBCommand.executeSelectOneQuery(statement, req);
            if (resultSet != null) {
                MailOverTimeDTO mdto = new MailOverTimeDTO();
                mdto.setImei(resultSet.getString("imei"));
                mdto.setMarque(resultSet.getString("marque"));
                mdto.setMatricule(resultSet.getString("matricule"));
                mdto.setSociete(resultSet.getString("raison_social"));
                mdto.setGsm(resultSet.getString("gsm"));
                return mdto;
            }
        } catch (Exception e) {
            Console.printStackTrace(e);

        }

        return null;
    }

    public DeviceTracking getNext(int idDevice, int idVehicule, String dateTracking) {
        try {


            Date tracking_date = Tools.DateTime.stringToDate(dateTracking);
            Date now = new Date();
            String table_name;
            long diffDate = now.getTime() - tracking_date.getTime();
            if (diffDate < Tools.DateTime.T.d * 2) {
                table_name = DbTables.tracking_three_days;
            } else {
                table_name = DbTables.tracking_three_months;
            }
            String req = " SELECT id_tracking, tracking_time, longitude, latitude,km, id_device,id_vehicule FROM " + table_name + " "
                    + " WHERE id_device=" + idDevice + " AND id_vehicule=" + idVehicule + "  AND tracking_time >'"
                    + dateTracking + "' ORDER BY tracking_time ASC LIMIT 0,1";
            Statement statement = DBCommand.getDbStatement();
            ResultSet resultSet = DBCommand.executeSelectOneQuery(statement, req);
            if (resultSet != null) {
                DeviceTracking tracking = new DeviceTracking();
                tracking.setId_tracking(resultSet.getInt("id_tracking"));
                tracking.setTracking_time(resultSet.getString("tracking_time"));
                tracking.setLongitude(resultSet.getDouble("longitude"));
                tracking.setLatitude(resultSet.getDouble("latitude"));
                tracking.setKm(resultSet.getDouble("km"));
                tracking.setId_device(resultSet.getInt("id_device"));
                tracking.setId_vehicule(resultSet.getInt("id_vehicule"));
                statement.close();
                resultSet.close();
                return tracking;
            }
        } catch (SQLException e) {
            Console.printStackTrace(e);
        }
        return null;
    }

    static class DbTables {
        static String tracking_three_days = "last_tracking_tdays";
        static String tracking_three_months = "last_tracking_tmonths";
        static String device = "device";
        static String tmp_over_time = "tmp_over_time";
    }

}
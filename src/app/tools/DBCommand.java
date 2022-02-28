package app.tools;


import app.model.Tracking;
import com.google.gson.Gson;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBCommand {


    public static Statement getDbStatement() throws SQLException {
        return ConnexionDB.getDbConnect().createStatement();
    }


    public static void execute(String req) {
        try {
//            ConnexionDB.getDbConnect().prepareStatement(req).execute();
            java.sql.Connection connexion = ConnexionDB.getDbConnect();
            PreparedStatement statement = connexion.prepareStatement(req);
            statement.execute();

        } catch (SQLException e) {
            Console.printStackTrace(e);
        }
    }

    public static long executeWithReturnId(String req) {
        try {
//            ConnexionDB.getDbConnect().prepareStatement(req).execute();
            java.sql.Connection connexion = ConnexionDB.getDbConnect();
            PreparedStatement statement = connexion.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
            statement.execute();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next())
                return generatedKeys.getLong(1);

        } catch (SQLException e) {
            Console.println(req, Console.Colors.getRED());
            e.printStackTrace();
        }
        return 0;
    }

    public static ResultSet executeSelectOneQuery(Statement statement, String req) {
        try {
            ResultSet resultSet = statement.executeQuery(req);
            if (!resultSet.next()) {
                resultSet.close();
                statement.close();
                return null;
            }
            return resultSet;
        } catch (SQLException e) {
            Console.printStackTrace(e);
            Console.println(req, Console.Colors.getRED());
            e.printStackTrace();
            return null;
        }
    }

    public static class DbInsert {
        private final ArrayList<String> columns = new ArrayList<>();
        private final ArrayList<String> values = new ArrayList<>();

        public static DbInsert prepareQuery(Tracking tracking) {
            DbInsert q = new DbInsert();
            q.addStr("tracking_time", tracking.getTracking_time());
            q.add("priority", tracking.getPriority());
            q.add("longitude", tracking.getLongitude());
            q.add("latitude", tracking.getLatitude());
            q.add("altitude", tracking.getAltitude());
            q.add("angle", tracking.getAngle());
//            System.out.println("imei:" + tracking.getImei() + " angle:" + tracking.getAngle());
            q.add("satellites", tracking.getSatellites());
            q.add("gps_speed", tracking.getGps_speed());
            q.add("acc_status", tracking.getAcc_status());
            q.add("speed", tracking.getSpeed());
            q.add("tracking_on_event", tracking.getTracking_on_event());
            q.add("event", tracking.getEvent());
            q.add("panic_button_status", tracking.getPanic_button_status());
            q.add("etat", tracking.getEtat());
            q.add("etatSignal", tracking.getEtatSignal());
//            if (!Double.isInfinite(tracking.getReservoir1()))
            q.add("reservoir1", tracking.getReservoir1());
//            if (!Double.isInfinite(tracking.getReservoir2()))
            q.add("reservoir2", tracking.getReservoir2());
            q.add("temperature1", tracking.getTemperature());
            q.add("temperature2", tracking.getTemperature2());
            q.add("km", tracking.getKm());
            q.add("movement", tracking.getMovement());
            q.add("input", tracking.getInput());
            q.add("id_device", tracking.getId_device());
            q.add("id_vehicule", tracking.getId_vehicule());
            q.add("actual_profile", tracking.getActual_profile());
            q.add("external_power", tracking.getExternal_power());
            q.add("internal_battery", tracking.getInternal_battery());
            q.add("current_battery", tracking.getCurrent_battery());
            q.add("pcb_temp", tracking.getPcb_temp());
            q.add("gps_pdop", tracking.getGps_pdop());
            q.add("gps_hdop", tracking.getGps_hdop());
            q.add("sleep_mode", tracking.getSleep_mode());
            q.add("operator_code", tracking.getOperator_code());
            q.add("area_code", tracking.getArea_code());
            q.add("cell_id", tracking.getCell_id());
            q.add("gnss_status", tracking.getGnss_status());
            q.add("rfid", tracking.getRfid());
            //
            q.add("can_speed", tracking.LVCAN_VEHICLE_SPEED);
            q.add("accelerator_pedal_position", tracking.LVCAN_ACCELERATOR_PEDAL_POSITION);
            q.add("can_fuel_level", tracking.LVCAN_FUEL_LEVEL);
            q.add("fuel_consumed", tracking.LVCAN_FUEL_CONSUMED);
            q.add("fuel_rate", tracking.LVCAN_FUEL_RATE);
            q.add("engine_rpm", tracking.LVCAN_ENGINE_RPM);
            q.add("total_mileage", tracking.LVCAN_TOTAL_MILEAGE);
            q.add("door_status", tracking.LVCAN_DOOR_STATUS);
            q.add("engine_temp", tracking.LVCAN_ENGINE_TEMPERATURE);
            q.add("engine_oil_level", tracking.ENGINE_OIL_LEVEL);
            q.add("engine_worktime", tracking.LVCAN_ENGINE_WORKTIME);
            q.add("ecodrive_type", tracking.GREEN_DRIVING_TYPE);
            q.add("ecodrive_value", tracking.GREEN_DRIVING_VALUE);
            q.add("ecodrive_duration", tracking.GREEN_DRIVING_EVENT_DURATION);
            q.add("crash_detect", tracking.CRASH_DETECTION);
            q.add("jaming", tracking.LVCAN_JAMMING);
            //
            q.add("alert_checked", tracking.alert_checked);
            q.add("distence_corrected", tracking.distance_corrected);
            //
            q.addStr("alv_data", new Gson().toJson(tracking.avlData));
            q.addStr("message", tracking.getMessage());
            return q;
        }


        private DbInsert addColumn(String column) {
            columns.add("`" + column + "`");
            return this;
        }

        private DbInsert addValue(String value, boolean isString) {

            if (!isString)
                values.add(value);
            else
                values.add("'" + value + "'");
            return this;
        }

        private DbInsert add(String column, String value) {
            return addColumn(column).addValue(value, false);
        }

        private void addStr(String column, String value) {
            addColumn(column).addValue(value, true);
        }


        private void add(String column, Boolean value) {
            add(column, value ? 1 : 0);
        }

        private void add(String column, Integer value) {
            add(column, String.valueOf(value));
        }

        private void add(String column, Double value) {
            add(column, String.valueOf(value));
        }

        public DbInsert add(String column, long value) {
            return add(column, String.valueOf(value));
        }

        public String getSQL() {
            return "INSERT INTO `{dbtTableName}`( " + String.join(",", columns) + " )  VALUES ( " + String.join(",", values) + " )";
        }


    }


}



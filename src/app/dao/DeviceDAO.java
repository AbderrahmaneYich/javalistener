package app.dao;

import app.model.Device;
import app.tools.Console;
import app.tools.DBCommand;

import java.sql.ResultSet;
import java.sql.Statement;

public class DeviceDAO {


    public Device getDevice(String imei) {

        try {
            Statement statement = DBCommand.getDbStatement();
            ResultSet resultSet = DBCommand.executeSelectOneQuery(statement, "SELECT * FROM `device` WHERE `IMEI` ='" + imei + "'");
            if (resultSet == null) {
                return null;
            } else {
                Device dev = new Device(resultSet.getInt("ID_Device"), resultSet.getString("IMEI"), resultSet.getInt("id_vehicule"));
                dev.volReservoir1Min = resultSet.getFloat("volReservoir1Min");
                dev.volReservoir1Max = resultSet.getFloat("volReservoir1Max");
                dev.volReservoir2Min = resultSet.getFloat("volReservoir2Min");
                dev.volReservoir2Max = resultSet.getFloat("volReservoir2Max");
                statement.close();
                resultSet.close();
                return dev;
            }
        } catch (Exception e1) {
            Console.printStackTrace(e1);
            return null;
        }
    }

}
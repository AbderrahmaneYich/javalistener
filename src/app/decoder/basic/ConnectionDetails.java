package app.decoder.basic;

import config.DeviceConfigs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//removable--#log
class ConnectionDetails {
    public final DeviceConfigs config;
    //    public final Date dateCo = new Date();
    final DateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    long startTime = System.currentTimeMillis();
    long logTime = System.currentTimeMillis();

    public String imei = null;
    //    public int nbrTracking = 0;
//    public Date dateDeco = null;
    public ArrayList<String> dialogMsg = new ArrayList<>();

    public void addLogMsg(String msg) {
        dialogMsg.add(msg);
    }

    public void addLogMsgWithDate(String msg) {
        addLogMsg(dt1.format(new Date()) + ": " + msg);
    }

    public void addLogMsgWithMs(String msg) {
        long endTime = System.currentTimeMillis();
        addLogMsg(msg + "  {{[ timeExec: " + (endTime - logTime) + "/" + (endTime - startTime) + "(ms)]}}");
        logTime = System.currentTimeMillis();
    }


    public ConnectionDetails(DeviceConfigs config) {
        this.config = config;
    }


    public void printLog() {
        this.config.logger.info("\n"+String.join("\n", dialogMsg));
    }
}
//removable--#log
//class ConnectedDevice {
//    private Date dateCo = new Date();
//    private String imei = null;
//    private ArrayList<ConnectionDetails> details = new ArrayList<>();
//
//    public ConnectedDevice(String imei) {
//        this.imei = imei;
//    }
//}

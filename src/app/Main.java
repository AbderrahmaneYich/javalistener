package app;

import app.dao.DeviceDAO;
import app.dao.TrackingDAO;
import app.tools.*;
import app.tools.avldata.AvlData;
import app.tools.avldata.AvlDataFM4;
import app.tools.avldata.AvlDataGH;
import app.tools.trackingDebugger.TemperatureCorrespondence;
import config.Env;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        boolean activateLogFile = false;
        try {
            for (String arg : args) {
                if ("-d".equals(arg)) {
                    activateLogFile = true;
                }
            }
        } catch (Exception e) {
            Console.printStackTrace(e);
        }
        Console.initLogger(activateLogFile);
        Env.printConfig();
        Console.println("####################################################################", Console.Colors.getRED());
        Console.println("####################################################################", Console.Colors.getGREEN());
        Console.printlnWithDate("starting Tracking Listener " + Env.app_version + "  ...");
        initDAO();
        ConnexionDB.start();
        registerCodecStoreInstances();
        TemperatureCorrespondence.init();
        MainListener.startAllListener();
        Console.printlnWithDate("The tracking listener has been fully activated", Console.Colors.getGREEN());
        Console.println("####################################################################", Console.Colors.getGREEN());
        Console.println("####################################################################", Console.Colors.getGREEN());
        Console.println("####################################################################", Console.Colors.getGREEN());
    }


    private static void initDAO() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        Tools.trackingDAO = context.getBean(TrackingDAO.class);
        Tools.deviceDAO = context.getBean(DeviceDAO.class);
    }

    private static void registerCodecStoreInstances() {
        CodecStore.register(AvlData.getCodec());
        CodecStore.register(AvlDataFM4.getCodec());
        CodecStore.register(AvlDataGH.getCodec());
    }
}
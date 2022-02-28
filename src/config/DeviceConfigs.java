package config;

import app.decoder.basic.DecoderDevice;
import app.tools.Console;
import app.tools.trackingDebugger.helpers.Debugger;

import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class DeviceConfigs {
    public final String name;
    public final int port;
    public final List<Debugger> Debuggers;
    public final boolean setKmUsingDistanceBetweenPrevious;
    private final DecoderDevice decoder;
    public String portState = "waiting";
    public Logger logger;

    DeviceConfigs(int port, String name, DecoderDevice decoder, boolean setKmUsingDistanceBetweenPrevious, List<Debugger> debuggers) {
        this.port = port;
        this.name = name;
        this.setKmUsingDistanceBetweenPrevious = setKmUsingDistanceBetweenPrevious;
        this.decoder = decoder;
        Debuggers = debuggers;
        //logger init
        logger = Logger.getLogger(name);
        FileHandler fh;
        try {
            String filePath = Env.getLoggerPath()+"/port-"+ port+".log";
            //create file
            ////////////////
            fh = new FileHandler(filePath,true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.info("the logger ready now ");
            Console.println("logger path "+filePath);

        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public DecoderDevice getDecoder() {
        try {
            DecoderDevice newInstance = decoder.getClass().newInstance();
            newInstance.port = port;
            return newInstance;
        } catch (InstantiationException | IllegalAccessException e) {
            Console.printStackTrace(e);
        }
        return null;
    }

    public void setPortState(String portState) {
        this.portState = portState;
        Console.printInSameLine("Processing: " + ListDevices.devicesPortState(), Console.Colors.getCYAN());

    }
}

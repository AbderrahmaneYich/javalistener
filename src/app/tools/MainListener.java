package app.tools;

import app.decoder.basic.Decoder;
import config.DeviceConfigs;
import config.ListDevices;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

public class MainListener {

    private static boolean allPortIsReady = false;
    private static ThreadPool threadPool = new ThreadPool();

    public static void startAllListener() {
        Console.println("opening ports to listening to the trackers requests", Console.Colors.getCYAN());
        ListDevices.devicesArray.forEach(deviceConfigs -> threadPool.getRunnables().add(() -> MainListener.tOpen(deviceConfigs)));

//        ListDevices.devicesArray.forEach(deviceConfigs -> {
//            if (deviceConfigs.port != 0)
//                threadPool.getRunnables().add(() -> MainListener.tOpen(deviceConfigs));
//        });


        threadPool.run();
        while (!allPortIsReady) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                Console.printStackTrace(e);
            }
        }

        Console.println(ListDevices.devicesPortState(), Console.Colors.getGREEN());
    }

    ///////////////////////////////////////////////////////////////
    private static void tOpen(DeviceConfigs deviceConfigs) {
        new Thread(() -> open(deviceConfigs)).start();
    }


    private static void open(DeviceConfigs deviceConfigs) {
        try {
            deviceConfigs.setPortState("opening");
            ServerSocket serverSocket = new ServerSocket(deviceConfigs.port);
            deviceConfigs.setPortState("opened");
            serverSocket.setSoTimeout(Tools.DateTime.T.m);//todo
            CheckIfAllPortIsReady();
            while (true) {
                try {
//                    Console.println("|||Waiting for connection from client Port:" + deviceConfigs.port+"     |||");
                    new Thread(new Decoder(serverSocket.accept())).start();
                } catch (SocketTimeoutException e) {
//                    Console.println("|||Err connection  Port:" + deviceConfigs.port+" msg: "+e.getMessage()+"|||");
//                    Console.printStackTrace(e);
                }
            }
        } catch (IOException e) {
            deviceConfigs.setPortState("closed");
            deviceConfigs.portState = Console.redText("err with opening port");
            Console.printStackTrace(e);
        }
    }

    private static void CheckIfAllPortIsReady() {
        if (ListDevices.devicesArray.size() == ListDevices.openedPortDevices().size()) {
            allPortIsReady = true;
        }
    }

//todo restart app
// close all cnx and waiting to ...  before restart

//    public void restartApplication()
//    {
//        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
//        final File currentJar = new File(MyClassInTheJar.class.getProtectionDomain().getCodeSource().getLocation().toURI());
//
//        /* is it a jar file? */
//        if(!currentJar.getName().endsWith(".jar"))
//            return;
//
//        /* Build command: java -jar application.jar */
//        final ArrayList<String> command = new ArrayList<String>();
//        command.add(javaBin);
//        command.add("-jar");
//        command.add(currentJar.getPath());
//
//        final ProcessBuilder builder = new ProcessBuilder(command);
//        builder.start();
//        System.exit(0);
//    }

}
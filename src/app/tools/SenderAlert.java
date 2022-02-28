package app.tools;

import app.model.DeviceTracking;

public class SenderAlert implements Runnable {

    private String dto;

    private SenderAlert(String dto) {
        this.dto = dto;
    }

    public static void checkAndHandler(long secTime, DeviceTracking tracking) {
        if (secTime < Tools.DateTime.T.d && tracking.getSpeed() < 250) {
            Thread thread = new Thread(new SenderAlert(Tools.convertType(tracking)));
            thread.setName("SenderAlert");
            thread.start();
        }
    }

    @Override
    public void run() {
        try {
            Tools.sendTracking(this.dto);
        } catch (Exception e) {
            Console.printStackTrace(e);
        }

    }

}
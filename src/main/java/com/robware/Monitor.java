package com.robware;

import com.robware.blink.BlinkNetworkAction;
import com.robware.blink.BlinkNetworkStateApi;
import com.robware.models.State;
import com.robware.notification.PushbulletManager;
import com.robware.util.Constants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Monitor {

    private final static String DEVICE_IP = "192.168.2.60";

    public static void main(String[] args) throws InterruptedException {
        PushbulletManager pushbulletManager = new PushbulletManager();

        try {
            while (true) {
                boolean isDeviceOnline = isDeviceOnline();
                updateBlink(getAction(isDeviceOnline));
                Thread.sleep(30000);
            }
        } catch(Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            pushbulletManager.push(Constants.PUSHBULLET_TITLE, "The blink app has failed. Here is the exception stack trace:\n\n" + sw);
            throw e;
        }
    }

    private static void updateBlink(BlinkNetworkAction updateAction) {
        System.out.println("Starting BlinkApp - action: " + updateAction);

        if(State.get().getBlinkPreviousAction() == updateAction) {
            System.out.println("Duplicate action - exiting");
            return;
        }

        System.out.println("Executing action: " + updateAction);
        new BlinkNetworkStateApi(updateAction.getUrlString()).call();

        // Update state
        State.updateAction(updateAction);

        System.out.println("Complete!");
    }

    private static BlinkNetworkAction getAction(boolean isDeviceOnline) {
        return isDeviceOnline ? BlinkNetworkAction.DISARM : BlinkNetworkAction.ARM;
    }

    public static boolean isDeviceOnline() {
        System.out.println("Checking if device is online...");
        String command = "ping " + DEVICE_IP;

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command.split("\\s+"));
            Process process = processBuilder.start();

            // To read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean isDeviceOnline = false;
            List<String> lines = new ArrayList<>();
            while ((line = reader.readLine()) != null && lines.size() < 10) {
                lines.add(line);
                if(line.toLowerCase().contains("bytes from " + DEVICE_IP)) {
                    isDeviceOnline = true;
                    break;
                }
                if(line.toLowerCase().contains("request timeout") || line.toLowerCase().contains("request timed out")) {
                    isDeviceOnline = false;
                    break;
                }
                if (line.toLowerCase().contains("unreachable")) {
                    isDeviceOnline = false;
                    break;
                }
                if (line.toLowerCase().startsWith("reply")) {
                    isDeviceOnline = true;
                    break;
                }
            } ///// CANNOT RELY ON STATE FOR ARMED - need to check? Maybe execute only if we did it last or something...

            if(lines.size() >= 10) {
                throw new RuntimeException("Couldn't determine if device was online. Input lines read: " + lines);
            }

            // End process
            process.destroy();
            System.out.println("Complete. Device online = " + isDeviceOnline);
            return isDeviceOnline;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

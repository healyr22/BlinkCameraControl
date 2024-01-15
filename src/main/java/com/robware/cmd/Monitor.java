package com.robware.cmd;

import com.robware.blink.BlinkApp;
import com.robware.blink.BlinkNetworkAction;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Monitor {

    public static void main(String[] args) throws InterruptedException {
        while(true) {
            boolean isDeviceOnline = isDeviceOnline();
            BlinkApp.update(getAction(isDeviceOnline));
            Thread.sleep(30000);
        }
    }

    private static BlinkNetworkAction getAction(boolean isDeviceOnline) {
        return isDeviceOnline ? BlinkNetworkAction.DISARM : BlinkNetworkAction.ARM;
    }

    public static boolean isDeviceOnline() {
        System.out.println("Checking if device is online...");
        String command = "ping 192.168.2.60";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command.split("\\s+"));
            Process process = processBuilder.start();

            // To read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean isDeviceOnline = false;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().startsWith("reply")) {
                    // Device is online
                    isDeviceOnline = !line.toLowerCase().contains("unreachable");
                    break;
                }
            } ///// CANNOT RELY ON STATE FOR ARMED - need to check? Maybe execute only if we did it last or something...

            // Wait for the process to finish
            int exitCode = process.waitFor();
            System.out.println("Complete. Device online = " + isDeviceOnline);
            return isDeviceOnline;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

package com.robware.notification;

import com.robware.util.InputUtil;

public class PushbulletManager {

    private final String targetDeviceId;

    public PushbulletManager() {
        PushbulletDevicesApi devicesApi = new PushbulletDevicesApi();
        PushbulletDevicesApi.Response devicesResponse = devicesApi.call();

        System.out.println("Here is a list of your Pushbullet devices:");
        for(int i = 0; i < devicesResponse.devices().size(); i++) {
            System.out.println((i+1) + ") " + devicesResponse.devices().get(i).nickname());
        }

        int choice = Integer.parseInt(InputUtil.getInput("Please choose a device to push to:").trim());
        this.targetDeviceId = devicesResponse.devices().get(choice-1).iden();
    }

    public void push(String title, String body) {
        PushbulletCreatePushApi createPushApi = new PushbulletCreatePushApi(
                new PushbulletCreatePushApi.Body(
                        "link",
                        title,
                        body,
                        this.targetDeviceId
                )
        );

        createPushApi.call();
    }

}

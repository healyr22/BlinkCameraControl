package com.robware.blink;

import com.robware.models.BlinkState;

public class BlinkApp {

    public static void update(BlinkNetworkAction updateAction) {
        System.out.println("Starting BlinkApp - action: " + updateAction);

        if(BlinkState.get().getPreviousAction() == updateAction) {
            System.out.println("Duplicate action - exiting");
            return;
        }

        System.out.println("Executing action: " + updateAction);
        new NetworkStateApi(updateAction.getUrlString()).call();

        // Update state
        BlinkState.updateAction(updateAction);

        System.out.println("Complete!");
    }
}
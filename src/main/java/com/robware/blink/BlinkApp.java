package com.robware.blink;

import com.robware.models.State;

public class BlinkApp {

    public static void update(BlinkNetworkAction updateAction) {
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
}
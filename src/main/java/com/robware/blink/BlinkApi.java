package com.robware.blink;

import com.robware.models.BlinkState;

public class BlinkApi {

    private final BlinkState blinkState;

    public BlinkApi() {
        this.blinkState = login();
    }

    public BlinkApi(BlinkState blinkState) {
        this.blinkState = blinkState;
    }

    public static BlinkState login() {
        return null;

    }

}

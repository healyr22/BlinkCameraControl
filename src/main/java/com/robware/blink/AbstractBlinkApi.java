package com.robware.blink;

import com.robware.models.BlinkState;
import com.robware.network.IApi;
import com.robware.util.InputUtil;

public abstract class AbstractBlinkApi implements IApi {

    @Override
    public boolean refreshAuth() {
        if(getName().equals(BlinkLoginApi.NAME)) {
            // Login call failed = refresh failed
            return false;
        }

        final String uuid = BlinkState.get().getUuid();

        // TODO add timeout for these
        var email = InputUtil.getInput("Please enter your Blink account email address:");
        var password = InputUtil.getInput("Please enter your password:");

        var loginApi = new BlinkLoginApi(new BlinkLoginApi.Body(
                uuid,
                email,
                password,
                true
        ));
        BlinkLoginApi.Response loginResponse = loginApi.call();

        // Update token
        BlinkState.updateAuthToken(loginResponse.auth().token());

        System.out.println("Successfully refreshed token");
        return true;
    }
}

package com.robware.blink;

import com.robware.models.BlinkState;
import com.robware.util.InputUtil;

import java.io.FileNotFoundException;
import java.util.UUID;

public class BlinkApp {

    public static void update(BlinkNetworkAction updateAction) {
        System.out.println("Starting BlinkApp - action: " + updateAction);

        BlinkState blinkState = initBlinkState();

        if(blinkState.getPreviousAction() == updateAction) {
            System.out.println("Duplicate action - exiting");
            return;
        }

        System.out.println("Executing action: " + updateAction);
        new NetworkStateApi(updateAction.getUrlString()).call();

        // Update state
        BlinkState.updateAction(updateAction);

        System.out.println("Complete!");
    }

    static BlinkState initBlinkState() {
        try {
            return BlinkState.getOrThrows();
        } catch(FileNotFoundException e) {
            System.out.println("File not found - initiate login");
        }

        var uuid = UUID.randomUUID().toString();
        var email = InputUtil.getInput("Please enter your Blink account email address:");
        var password = InputUtil.getInput("Please enter your password:");

        var loginApi = new LoginApi(new LoginApi.Body(
                uuid,
                email,
                password,
                true
        ));
        LoginApi.Response loginResponse = loginApi.call();

        if(loginResponse.account().client_verification_required()) {
            String pin = InputUtil.getInput("Please enter the code sent to your email or phone:");
            var verifyApi = new VerifyClientApi(loginResponse.account().account_id(), loginResponse.account().client_id(), loginResponse.account().tier(), loginResponse.auth().token(), new VerifyClientApi.Body(pin));
            verifyApi.call();
        }

        HomeScreenApi hsApi = new HomeScreenApi(
                loginResponse.account().account_id(),
                loginResponse.account().tier(),
                loginResponse.auth().token());

        HomeScreenApi.Response hsResponse = hsApi.call();

        var blinkState = new BlinkState(
                loginResponse.auth().token(),
                uuid,
                loginResponse.account().account_id(),
                loginResponse.account().client_id(),
                loginResponse.account().tier(),
                hsResponse.networks().get(0).id(),
                null);
        BlinkState.save(blinkState);

        return blinkState;
    }
}
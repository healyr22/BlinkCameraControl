package com.robware.models;

import com.robware.blink.BlinkHomeScreenApi;
import com.robware.blink.BlinkLoginApi;
import com.robware.blink.BlinkNetworkAction;
import com.robware.blink.BlinkVerifyClientApi;
import com.robware.util.InputUtil;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class State {

    private String blinkAuthToken;

    private String blinkUuid;

    private String blinkAccountId;

    private String blinkClientId;

    private String blinkTier;

    private String blinkNetworkId;

    private BlinkNetworkAction blinkPreviousAction;

    private String pushbulletAccessToken;

    private static State instance;

    public static State get() {
        if(instance == null) {
            init();
        }
        return instance;
    }

    public static void set(State state) {
        instance = state;
    }

    public static void updateAction(BlinkNetworkAction updateAction) {
        State currentState = get();
        set(currentState.toBuilder()
                .blinkPreviousAction(updateAction)
                .build());
    }

    public static void updateAuthToken(String authToken) {
        State currentState = get();
        set(currentState.toBuilder()
                .blinkAuthToken(authToken)
                .build());
    }

    private static void init() {
        System.out.println("Initializing state - starting login");

        var uuid = UUID.randomUUID().toString();
        var email = InputUtil.getInput("Please enter your Blink account email address:");
        var password = InputUtil.getInput("Please enter your password:");
        var pushbulletAccessToken = InputUtil.getInput("Please enter your pushbullet access token:");

        var loginApi = new BlinkLoginApi(new BlinkLoginApi.Body(
                uuid,
                email,
                password,
                true
        ));
        BlinkLoginApi.Response loginResponse = loginApi.call();

        if(loginResponse.account().client_verification_required()) {
            String pin = InputUtil.getInput("Please enter the code sent to your email or phone:");
            var verifyApi = new BlinkVerifyClientApi(loginResponse.account().account_id(), loginResponse.account().client_id(), loginResponse.account().tier(), loginResponse.auth().token(), new BlinkVerifyClientApi.Body(pin));
            verifyApi.call();
        }

        BlinkHomeScreenApi hsApi = new BlinkHomeScreenApi(
                loginResponse.account().account_id(),
                loginResponse.account().tier(),
                loginResponse.auth().token());

        BlinkHomeScreenApi.Response hsResponse = hsApi.call();

        State.set(new State(
                loginResponse.auth().token(),
                uuid,
                loginResponse.account().account_id(),
                loginResponse.account().client_id(),
                loginResponse.account().tier(),
                hsResponse.networks().get(0).id(),
                null,
                pushbulletAccessToken
        ));
    }
}

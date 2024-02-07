package com.robware.models;

import com.robware.blink.BlinkNetworkAction;
import com.robware.blink.BlinkHomeScreenApi;
import com.robware.blink.BlinkLoginApi;
import com.robware.blink.BlinkVerifyClientApi;
import com.robware.util.InputUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class BlinkState {

    private String authToken;

    private String uuid;

    private String accountId;

    private String clientId;

    private String tier;

    private String networkId;

    private BlinkNetworkAction previousAction;

    private static BlinkState instance;

    public static BlinkState get() {
        if(instance == null) {
            init();
        }
        return instance;
    }

    public static void set(BlinkState state) {
        instance = state;
    }

    public static void updateAction(BlinkNetworkAction updateAction) {
        BlinkState currentState = get();
        set(new BlinkState(
                currentState.getAuthToken(),
                currentState.getUuid(),
                currentState.getAccountId(),
                currentState.getClientId(),
                currentState.getTier(),
                currentState.getNetworkId(),
                updateAction));
    }

    public static void updateAuthToken(String authToken) {
        BlinkState currentState = get();
        set(new BlinkState(
                authToken,
                currentState.getUuid(),
                currentState.getAccountId(),
                currentState.getClientId(),
                currentState.getTier(),
                currentState.getNetworkId(),
                currentState.getPreviousAction()));
    }

    private static void init() {
        System.out.println("Initializing state - starting login");

        var uuid = UUID.randomUUID().toString();
        var email = InputUtil.getInput("Please enter your Blink account email address:");
        var password = InputUtil.getInput("Please enter your password:");

        var loginApi = new BlinkLoginApi(new BlinkLoginApi.Body(
                uuid,
                email,
                password,
                true
        ));
        BlinkLoginApi.Response loginResponse = loginApi.call();

        if(loginResponse.getAccount().isClient_verification_required()) {
            String pin = InputUtil.getInput("Please enter the code sent to your email or phone:");
            var verifyApi = new BlinkVerifyClientApi(
                    loginResponse.getAccount().getAccount_id(),
                    loginResponse.getAccount().getClient_id(),
                    loginResponse.getAccount().getTier(),
                    loginResponse.getAuth().getToken(),
                    new BlinkVerifyClientApi.Body(pin));
            verifyApi.call();
        }

        BlinkHomeScreenApi hsApi = new BlinkHomeScreenApi(
                loginResponse.getAccount().getAccount_id(),
                loginResponse.getAccount().getTier(),
                loginResponse.getAuth().getToken());

        BlinkHomeScreenApi.Response hsResponse = hsApi.call();

        var blinkState = new BlinkState(
                loginResponse.getAuth().getToken(),
                uuid,
                loginResponse.getAccount().getAccount_id(),
                loginResponse.getAccount().getClient_id(),
                loginResponse.getAccount().getTier(),
                hsResponse.getNetworks().get(0).getId(),
                null);
        BlinkState.set(blinkState);

        instance = blinkState;
    }
}

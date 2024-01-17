package com.robware.models;

import com.robware.blink.BlinkNetworkAction;
import com.robware.blink.HomeScreenApi;
import com.robware.blink.LoginApi;
import com.robware.blink.VerifyClientApi;
import com.robware.json.JsonMapper;
import com.robware.util.InputUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
        BlinkState.set(blinkState);

        instance = blinkState;
    }
}

package com.robware;

import com.robware.blink.HomeScreenApi;
import com.robware.blink.LoginApi;
import com.robware.blink.VerifyClientApi;
import com.robware.json.JsonMapper;
import com.robware.models.BlinkState;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    private static final String STATE_FILE_NAME = "state.json";

    public static void main(String[] args) throws IOException {
        System.out.println("Starting program");

        Scanner s = new Scanner(System.in);
        BlinkState blinkState = getBlinkState(s);
    }

    static BlinkState getBlinkState(Scanner s) {
        try {
            return JsonMapper.mapper().readValue(new File(STATE_FILE_NAME), BlinkState.class);
        } catch(FileNotFoundException e) {
            System.out.println("File not found - initiate login");
        } catch(Exception e) {
            throw new RuntimeException("Error reading file", e);
        }

        var uuid = UUID.randomUUID().toString();
        var email = getInput(s, "Please enter your Blink account email address:");
        var password = getInput(s, "Please enter your password:");

        var loginApi = new LoginApi(new LoginApi.Body(
                uuid,
                email,
                password,
                true
        ));
        LoginApi.Response loginResponse = loginApi.call();

        if(loginResponse.account().client_verification_required()) {
            String pin = getInput(s, "Please enter the code sent to your email or phone:");
            var verifyApi = new VerifyClientApi(loginResponse.account().account_id(), loginResponse.account().client_id(), loginResponse.account().tier(), loginResponse.auth().token(), new VerifyClientApi.Body(pin));
            verifyApi.call();
        }

        HomeScreenApi hsApi = new HomeScreenApi(loginResponse.account().account_id(), loginResponse.account().tier(), loginResponse.auth().token());
        HomeScreenApi.Response hsResponse = hsApi.call();

        var blinkState = new BlinkState(loginResponse.auth().token(), uuid, loginResponse.account().account_id(), loginResponse.account().client_id(), loginResponse.account().tier(), hsResponse.networks().get(0).id());

        try {
            String stateString = JsonMapper.mapper().writeValueAsString(blinkState);
            Files.writeString(
                    Paths.get(STATE_FILE_NAME),
                    stateString,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch(Exception e) {
            throw new RuntimeException("Unable to write file", e);
        }


        s.close();
        return blinkState;
    }

    static String getInput(Scanner s, String msg) {
        System.out.println(msg);
        return s.nextLine();
    }
}
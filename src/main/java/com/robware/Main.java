package com.robware;

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

        BlinkState blinkState = getBlinkState();

    }

    static BlinkState getBlinkState() {
        try {
            return JsonMapper.mapper().readValue(new File(STATE_FILE_NAME), BlinkState.class);
        } catch(FileNotFoundException e) {
            System.out.println("File not found - initiate login");
        } catch(Exception e) {
            throw new RuntimeException("Error reading file", e);
        }

        var uuid = UUID.randomUUID().toString();
        var email = getInput("Please enter your Blink account email address:");
        var password = getInput("Please enter your password:");

        var loginApi = new LoginApi(new LoginApi.Body(
                uuid,
                email,
                password,
                true
        ));
        LoginApi.Response response = loginApi.call();
        var blinkState = new BlinkState(response.auth().token(), uuid, response.account().account_id(), response.account().client_id(), response.account().tier());

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

        if(response.account().client_verification_required()) {
            String pin = getInput("Please enter the code sent to your email or phone:");
            var verifyApi = new VerifyClientApi(blinkState, new VerifyClientApi.Body(pin));
            verifyApi.call();
        }

        return blinkState;
    }

    static String getInput(String msg) {
        System.out.println(msg);
        try(Scanner s = new Scanner(System.in)) {
            return s.nextLine();
        }
    }
}
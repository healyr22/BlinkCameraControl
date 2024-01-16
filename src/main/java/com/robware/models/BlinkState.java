package com.robware.models;

import com.robware.blink.BlinkNetworkAction;
import com.robware.json.JsonMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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

    private static final String STATE_FILE_NAME = "state.json";

    private static BlinkState instance;

    public static BlinkState getOrThrows() throws FileNotFoundException {
        if(instance == null) {
            try {
                instance = JsonMapper.mapper().readValue(new File(STATE_FILE_NAME), BlinkState.class);
            } catch (FileNotFoundException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException("Error reading file", e);
            }
        }

        return instance;
    }

    public static BlinkState get() {
        try {
            return getOrThrows();
        } catch(FileNotFoundException e) {
            throw new RuntimeException("State file not found", e);
        }
    }

    public static void save(BlinkState state) {
        try {
            String stateString = JsonMapper.mapper().writeValueAsString(state);
            Files.writeString(
                    Paths.get(STATE_FILE_NAME),
                    stateString,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
            instance = state;
        } catch(Exception e) {
            throw new RuntimeException("Unable to write file", e);
        }
    }

    public static void updateAction(BlinkNetworkAction updateAction) {
        BlinkState currentState = get();
        save(new BlinkState(
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
        save(new BlinkState(
                authToken,
                currentState.getUuid(),
                currentState.getAccountId(),
                currentState.getClientId(),
                currentState.getTier(),
                currentState.getNetworkId(),
                currentState.getPreviousAction()));
    }
}

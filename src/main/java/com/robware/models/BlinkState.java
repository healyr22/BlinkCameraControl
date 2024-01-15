package com.robware.models;

import com.robware.blink.BlinkNetworkAction;

public record BlinkState(
        String authToken,
        String uuid,
        String accountId,
        String clientId,
        String tier,
        String networkId,
        BlinkNetworkAction previousAction) {
}

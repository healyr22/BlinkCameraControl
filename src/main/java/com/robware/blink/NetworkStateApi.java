package com.robware.blink;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.robware.json.JsonMapper;
import com.robware.models.BlinkState;

import java.util.ArrayList;
import java.util.List;

public class NetworkStateApi implements IBlinkApi {

    public static String NAME = "NETWORK_STATE_API";

    private final BlinkState state;
    private final String action;

    public NetworkStateApi(BlinkState state, String action) {
        this.state = state;
        this.action = action;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getApiUrl() {
        return BlinkConstants.getTierUrl(state.tier()) +
                "/api/v1/accounts/" +
                state.accountId() +
                "/networks/" +
                state.networkId() +
                "/state/" +
                action;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String[] getHeaders() {
        var headers = new ArrayList<>(List.of(BlinkConstants.CONTENT_TYPE_JSON));
        headers.add(BlinkConstants.AUTH_HEADER);
        headers.add(state.authToken());
        return headers.toArray(new String[4]);
    }

    @Override
    public Class<Object> getResponseClass() {
        return Object.class;
    }
}

package com.robware.blink;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.robware.json.JsonMapper;
import com.robware.models.BlinkState;

import java.util.ArrayList;
import java.util.List;

public class VerifyClientApi implements IBlinkApi {
//https://rest-u020.immedia-semi.com/api/v4/account/25739/client/1039644/pin/verify
    public record Body(String pin) {}

    public static String NAME = "VERIFY_CLIENT_API";

    private final BlinkState state;
    private final Body body;

    public VerifyClientApi(BlinkState state, Body body) {
        this.state = state;
        this.body = body;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getApiUrl() {
        return BlinkConstants.getTierUrl(state.tier()) +
                "/api/v4/account/" +
                state.account_id() +
                "/client/" +
                state.client_id() +
                "/pin/verify";
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String[] getHeaders() {
        var headers = new ArrayList<>(List.of(BlinkConstants.CONTENT_TYPE_JSON));
        headers.add(BlinkConstants.AUTH_HEADER);
        headers.add(this.state.authToken());
        return headers.toArray(new String[4]);
    }

    @Override
    public String getBody() {
        try {
            return JsonMapper.mapper().writeValueAsString(this.body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<Object> getResponseClass() {
        return Object.class;
    }
}

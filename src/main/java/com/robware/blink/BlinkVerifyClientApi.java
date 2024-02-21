package com.robware.blink;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.robware.json.JsonMapper;
import com.robware.network.HttpMethod;
import com.robware.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class BlinkVerifyClientApi extends AbstractBlinkApi {

    public record Body(String pin) {}

    public static String NAME = "VERIFY_CLIENT_API";

    private final String accountId, clientId, tier, authToken;
    private final Body body;

    public BlinkVerifyClientApi(String accountId, String clientId, String tier, String authToken, Body body) {
        this.accountId = accountId;
        this.clientId = clientId;
        this.tier = tier;
        this.authToken = authToken;
        this.body = body;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getApiUrl() {
        return Constants.getBlinkTierUrl(tier) +
                "/api/v4/account/" +
                accountId +
                "/client/" +
                clientId +
                "/pin/verify";
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String[] getHeaders() {
        var headers = new ArrayList<>(List.of(Constants.CONTENT_TYPE_JSON));
        headers.add(Constants.BLINK_AUTH_HEADER);
        headers.add(authToken);
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

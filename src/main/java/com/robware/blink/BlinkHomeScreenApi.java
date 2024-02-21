package com.robware.blink;

import com.robware.network.HttpMethod;
import com.robware.util.Constants;

import java.util.List;

public class BlinkHomeScreenApi extends AbstractBlinkApi {

    public record Response(List<Network> networks) {
        public record Network(String id) {}
    }

    public static String NAME = "HOME_SCREEN_API";

    private final String accountId, tier, authToken;

    public BlinkHomeScreenApi(String accountId, String tier, String authToken) {
        this.accountId = accountId;
        this.tier = tier;
        this.authToken = authToken;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getApiUrl() {
        return Constants.getBlinkTierUrl(tier) +
                "/api/v3/accounts/" +
                accountId +
                "/homescreen";
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String[] getHeaders() {
        return List.of(Constants.BLINK_AUTH_HEADER, authToken).toArray(new String[2]);
    }

    @Override
    public Class<Response> getResponseClass() {
        return Response.class;
    }
}

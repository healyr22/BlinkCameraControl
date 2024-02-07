package com.robware.blink;

import com.robware.network.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class BlinkHomeScreenApi extends AbstractBlinkApi {

    @AllArgsConstructor
    @Getter
    public static class Response {
        private List<Network> networks;

        @AllArgsConstructor
        @Getter
        public static class Network {
            private String id;
        }
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
        return BlinkConstants.getTierUrl(tier) +
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
        return List.of(BlinkConstants.AUTH_HEADER, authToken).toArray(new String[2]);
    }

    @Override
    public Class<Response> getResponseClass() {
        return Response.class;
    }
}

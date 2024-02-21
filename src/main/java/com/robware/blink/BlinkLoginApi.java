package com.robware.blink;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.robware.json.JsonMapper;
import com.robware.network.HttpMethod;
import com.robware.util.Constants;

public class BlinkLoginApi extends AbstractBlinkApi {

    public record Body(String unique_id, String email, String password, boolean reauth) {}

    public record Response(Account account, Auth auth) {
        public record Account(String account_id, String client_id, String tier, boolean client_verification_required) {}
        public record Auth(String token) {}
    }

    public static String NAME = "BLINK_LOGIN_API";
    public static String LOGIN_API = Constants.BLINK_PROD_URL + "/api/v5/account/login";

    private final Body body;

    public BlinkLoginApi(Body body) {
        this.body = body;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getApiUrl() {
        return LOGIN_API;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String[] getHeaders() {
        return Constants.CONTENT_TYPE_JSON;
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
    public Class<Response> getResponseClass() {
        return Response.class;
    }
}

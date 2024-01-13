package com.robware.blink;

public class LoginApi extends AbstractApi {

    public record Response(Account account, Auth auth) {
        record Account(String account_id, String client_id, String tier, boolean client_verification_required) {}
        record Auth(String token) {}
    }

    public static String NAME = "LOGIN_API";
    public static String LOGIN_API = "/api/v5/account/login";

    @Override
    String getName() {
        return NAME;
    }

    @Override
    String getApiUrl() {
        return LOGIN_API;
    }

    @Override
    HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    String[] getHeaders() {
        return BlinkConstants.CONTENT_TYPE_JSON;
    }
}

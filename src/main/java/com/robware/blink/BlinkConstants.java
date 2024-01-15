package com.robware.blink;

public class BlinkConstants {

    public static String PROD_URL = "https://rest-prod.immedia-semi.com";

    public static String getTierUrl(String tier) {
        return "https://rest-" + tier + ".immedia-semi.com";
    }

    public static String LOGIN_API = "/api/v5/account/login";

    public static String getVerifyClientApi(String accountId, String clientId) {
        return "/api/v4/account/" +
                accountId +
                "/client/" +
                clientId +
                "/pin/verify";
    }

    public static String getHomescreenApi(String accountId) {
        return "/api/v3/accounts/" +
                accountId +
                "/homescreen";
    }

    public static String getNetworkActionApi(String accountId, String networkId, BlinkNetworkAction action) {
        return "/api/v1/accounts/" +
                accountId +
                "/networks/" +
                networkId +
                "/state/" +
                action.getUrlString();
    }
    public static String ARM_API = "/api/v1/accounts/{{BLINK_ACCOUNT_ID}}/networks/{{BLINK_NETWORK_ID}}/state/disarm";

    // Headers
    public static String[] CONTENT_TYPE_JSON = new String[] {"Content-Type", "application/json"};
    public static String AUTH_HEADER = "token-auth";

}

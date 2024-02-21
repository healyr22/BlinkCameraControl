package com.robware.util;

public class Constants {

    public static String BLINK_PROD_URL = "https://rest-prod.immedia-semi.com";

    public static String getBlinkTierUrl(String tier) {
        return "https://rest-" + tier + ".immedia-semi.com";
    }
    public static String PUSHBULLET_PUSH_TYPE_NOTE = "note";
    public static String PUSHBULLET_TITLE = "Blink App Error";

    // Headers
    public static String[] CONTENT_TYPE_JSON = new String[] {"Content-Type", "application/json"};
    public static String BLINK_AUTH_HEADER = "token-auth";
    public static String PUSHBULLET_AUTH_HEADER = "Access-Token";

}

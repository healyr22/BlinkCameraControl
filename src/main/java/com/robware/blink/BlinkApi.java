package com.robware.blink;

import com.robware.models.LoginDetails;

import java.net.URI;
import java.net.http.HttpClient;

public class BlinkApi {

    private final LoginDetails loginDetails;

    public BlinkApi() {
        this.loginDetails = login();
    }

    public BlinkApi(LoginDetails loginDetails) {
        this.loginDetails = loginDetails;
    }

    public static LoginDetails login() {
        return null;

    }

}

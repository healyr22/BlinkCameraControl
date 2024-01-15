package com.robware.blink;

public enum BlinkNetworkAction {
    ARM("arm"),
    DISARM("disarm");

    private String urlString;

    BlinkNetworkAction(String urlString) {
        this.urlString = urlString;
    }

    public String getUrlString() {
        return this.urlString;
    }
}

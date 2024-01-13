package com.robware.blink;

public enum NetworkAction {
    ARM("arm"),
    DISARM("disarm");

    private String urlString;

    NetworkAction(String urlString) {
        this.urlString = urlString;
    }

    public String getUrlString() {
        return this.urlString;
    }
}

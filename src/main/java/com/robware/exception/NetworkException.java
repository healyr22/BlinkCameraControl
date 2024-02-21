package com.robware.exception;

import lombok.Getter;

@Getter
public class NetworkException extends RuntimeException {

    private final int statusCode;

    public NetworkException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public NetworkException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }
}

package com.robware.externalip;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.robware.network.HttpMethod;
import com.robware.network.IApi;

public class ExternalIpApi implements IApi {

    private static final String URL = "http://api.ipify.org";
    public static String NAME = "EXTERNAL_IP_API";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getApiUrl() {
        return URL;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public Class<String> getResponseClass() {
        return String.class;
    }

    @Override
    public String parseRawResponse(String rawResponse) throws JsonProcessingException {
        return rawResponse;
    }
}

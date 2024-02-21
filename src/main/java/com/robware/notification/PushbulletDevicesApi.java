package com.robware.notification;

import com.robware.models.State;
import com.robware.network.HttpMethod;
import com.robware.network.IApi;
import com.robware.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class PushbulletDevicesApi implements IApi {

    public record Response(List<Device> devices) {
        public record Device(String iden, String nickname) {}
    }

    private static final String URL = "https://api.pushbullet.com/v2/devices";
    public static String NAME = "PUSHBULLET_DEVICES_API";

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
    public String[] getHeaders() {
        var state = State.get();
        var headers = new ArrayList<>();
        headers.add(Constants.PUSHBULLET_AUTH_HEADER);
        headers.add(state.getPushbulletAccessToken());
        return headers.toArray(new String[2]);
    }

    @Override
    public Class<Response> getResponseClass() {
        return Response.class;
    }
}

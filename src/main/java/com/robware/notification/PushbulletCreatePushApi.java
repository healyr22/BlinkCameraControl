package com.robware.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.robware.json.JsonMapper;
import com.robware.models.State;
import com.robware.network.HttpMethod;
import com.robware.network.IApi;
import com.robware.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class PushbulletCreatePushApi implements IApi {

    public record Body(String type, String title, String body, String device_iden) {}

    private static final String URL = "https://api.pushbullet.com/v2/pushes";
    public static String NAME = "PUSHBULLET_CREATE_PUSH_API";

    private final Body body;

    public PushbulletCreatePushApi(Body body) {
        this.body = body;
    }

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
        return HttpMethod.POST;
    }

    @Override
    public String[] getHeaders() {
        var state = State.get();
        var headers = new ArrayList<>(List.of(Constants.CONTENT_TYPE_JSON));
        headers.add(Constants.PUSHBULLET_AUTH_HEADER);
        headers.add(state.getPushbulletAccessToken());
        return headers.toArray(new String[2]);
    }

    @Override
    public String getBody() {
        try {
//            System.out.println("[ROB_DEBUG] Sending " + JsonMapper.mapper().writeValueAsString(this.body));
            return JsonMapper.mapper().writeValueAsString(this.body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<Object> getResponseClass() {
        return Object.class;
    }
}

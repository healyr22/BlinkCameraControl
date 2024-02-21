package com.robware.blink;
import com.robware.models.State;
import com.robware.network.HttpMethod;
import com.robware.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class BlinkNetworkStateApi extends AbstractBlinkApi {

    public static String NAME = "NETWORK_STATE_API";
    private final String action;

    public BlinkNetworkStateApi(String action) {
        this.action = action;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getApiUrl() {
        final var state = State.get();
        return Constants.getBlinkTierUrl(state.getBlinkTier()) +
                "/api/v1/accounts/" +
                state.getBlinkAccountId() +
                "/networks/" +
                state.getBlinkNetworkId() +
                "/state/" +
                action;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String[] getHeaders() {
        final var state = State.get();
        var headers = new ArrayList<>(List.of(Constants.CONTENT_TYPE_JSON));
        headers.add(Constants.BLINK_AUTH_HEADER);
        headers.add(state.getBlinkAuthToken());
        return headers.toArray(new String[4]);
    }

    @Override
    public Class<Object> getResponseClass() {
        return Object.class;
    }
}

package com.robware.blink;
import com.robware.models.BlinkState;
import com.robware.network.HttpMethod;

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
        final var state = BlinkState.get();
        return BlinkConstants.getTierUrl(state.getTier()) +
                "/api/v1/accounts/" +
                state.getAccountId() +
                "/networks/" +
                state.getNetworkId() +
                "/state/" +
                action;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String[] getHeaders() {
        final var state = BlinkState.get();
        var headers = new ArrayList<>(List.of(BlinkConstants.CONTENT_TYPE_JSON));
        headers.add(BlinkConstants.AUTH_HEADER);
        headers.add(state.getAuthToken());
        return headers.toArray(new String[4]);
    }

    @Override
    public Class<Object> getResponseClass() {
        return Object.class;
    }
}

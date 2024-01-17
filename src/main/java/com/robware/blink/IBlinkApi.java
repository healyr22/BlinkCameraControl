package com.robware.blink;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.robware.json.JsonMapper;
import com.robware.models.BlinkState;
import com.robware.util.InputUtil;
import org.javatuples.Pair;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public interface IBlinkApi {

    int MAX_RETRIES = 4;
    int RETRY_COOLDOWN_SECONDS = 30;

    default <ResponseT> ResponseT call() {
        return callWithRetries(0);
    }

    private <ResponseT> ResponseT callWithRetries(int retryCount) {
        // Call API and retry in case token is expired

        try {
            Pair<Integer, String> response = call0();
            if(response.getValue0() == 401 && !getName().equals(LoginApi.NAME)) {
                System.out.println("Token has expired - refreshing token");

                final String uuid = BlinkState.get().getUuid();

                var email = InputUtil.getInput("Please enter your Blink account email address:");
                var password = InputUtil.getInput("Please enter your password:");

                var loginApi = new LoginApi(new LoginApi.Body(
                        uuid,
                        email,
                        password,
                        true
                ));
                LoginApi.Response loginResponse = loginApi.call();

                // Update token
                BlinkState.updateAuthToken(loginResponse.auth().token());

                System.out.println("Successfully refreshed token, retrying call...");
                response = call0();
            }

            if (response.getValue0() == 200) {
                return JsonMapper.mapper().readValue(response.getValue1(), getResponseClass());
            }

            // Error response
            if(retryCount < MAX_RETRIES) {
                // Wait then retry call
                System.out.println("Request failed. Will retry in " + RETRY_COOLDOWN_SECONDS + " seconds...");
                Thread.sleep(RETRY_COOLDOWN_SECONDS * 1000);
                System.out.println("Retrying " + (MAX_RETRIES-retryCount) + " more time(s)...");
                return callWithRetries(retryCount + 1);
            }

            throw new RuntimeException("Error response from API. Code: " + response.getValue0() + " - Response: " + response.getValue1());

        } catch(JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON response", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Pair<Integer, String> call0() {
        HttpClient httpClient = HttpClient.newHttpClient();

        String apiUrl = getApiUrl();
        URI uri = URI.create(apiUrl);

        var body = getBody();
        if(body == null) {
            body = "";
        }
        var bodyPublisher = HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .method(getMethod().name(), bodyPublisher)
                .headers(getHeaders())
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Process the response
            int statusCode = response.statusCode();
            String responseBody = response.body();

            System.out.println("Status Code: " + statusCode);
            System.out.println("Response Body: " + responseBody);

            return Pair.with(statusCode, responseBody);

        } catch (Exception e) {
            throw new RuntimeException("Error calling api: " + getName(), e);
        }
    }

    <T> Class<T> getResponseClass();

    String getName();

    String getApiUrl();

    HttpMethod getMethod();

    String[] getHeaders();

    default String getBody() {
        return null;
    }

}

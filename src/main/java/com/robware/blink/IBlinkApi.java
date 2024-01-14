package com.robware.blink;

import com.robware.json.JsonMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public interface IBlinkApi {

    default <ResponseT> ResponseT call() {
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

            return JsonMapper.mapper().readValue(responseBody, getResponseClass());

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

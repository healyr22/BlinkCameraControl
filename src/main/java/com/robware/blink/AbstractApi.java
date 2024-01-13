package com.robware.blink;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class AbstractApi<ResponseT> {

    public ResponseT call() {
        HttpClient httpClient = HttpClient.newHttpClient();

        String apiUrl = getApiUrl();
        URI uri = URI.create(apiUrl);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .headers(getHeaders())
                .method(getMethod().name(), null)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Process the response
            int statusCode = response.statusCode();
            String responseBody = response.body();

            System.out.println("Status Code: " + statusCode);
            System.out.println("Response Body: " + responseBody);

            TypeReference<ResponseT> typeRef = new TypeReference<>() {};
            return new ObjectMapper().readValue(responseBody, typeRef);

        } catch (Exception e) {
            throw new RuntimeException("Error calling api: " + getName(), e);
        }
    }

    abstract String getName();

    abstract String getApiUrl();

    abstract HttpMethod getMethod();
    abstract String[] getHeaders();

}

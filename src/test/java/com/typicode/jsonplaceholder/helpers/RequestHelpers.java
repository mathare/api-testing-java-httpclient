package com.typicode.jsonplaceholder.helpers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestHelpers {

    private static final HttpClient httpClient = HttpClient.newBuilder().build();
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static HttpResponse<String> sendGetRequestTo(String endpoint) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASE_URL + endpoint))
                .build();
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HttpResponse<String> sendPutRequestTo(String endpoint, String body) {
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .uri(URI.create(BASE_URL + endpoint))
                .build();
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HttpResponse<String> sendPostRequestTo(String endpoint, String body) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .uri(URI.create(BASE_URL + endpoint))
                .build();
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HttpResponse<String> sendDeleteRequestTo(String endpoint) {
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(BASE_URL + endpoint))
                .build();
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String buildQueryParamsString(Map<String, String> params) {
        StringBuilder paramString = new StringBuilder();
        List<String> keys = new ArrayList<>(params.keySet());
        for (String key : keys) {
            paramString.append("&").append(key).append("=").append(params.get(key));
        }
        return "?" + paramString.substring(1);
    }
}

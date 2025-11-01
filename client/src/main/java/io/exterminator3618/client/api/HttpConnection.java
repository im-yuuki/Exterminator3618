package io.exterminator3618.client.api;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;

interface HttpConnection {

    HttpClient getHttpClient();

    String getBaseServerUrl();

    String jsonSerializeObject(Object o);

    default HttpRequest createGetRequest(String endpoint) {
        return HttpRequest.newBuilder()
                .uri(java.net.URI.create(getBaseServerUrl() + endpoint))
                .GET()
                .build();
    }

    default HttpRequest createJsonPostRequest(String endpoint, Object body) {
        String serializedBody = jsonSerializeObject(body);
        return HttpRequest.newBuilder()
                .uri(java.net.URI.create(getBaseServerUrl() + endpoint))
                .POST(HttpRequest.BodyPublishers.ofString(serializedBody))
                .header("Content-Type", "application/json")
                .build();
    }

    default HttpRequest createJsonPatchRequest(String endpoint, Object body) {
        String serializedBody = jsonSerializeObject(body);
        return HttpRequest.newBuilder()
                .uri(java.net.URI.create(getBaseServerUrl() + endpoint))
                .method("PATCH", HttpRequest.BodyPublishers.ofString(serializedBody))
                .header("Content-Type", "application/json")
                .build();
    }

}

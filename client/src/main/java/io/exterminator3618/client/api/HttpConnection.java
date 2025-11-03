package io.exterminator3618.client.api;

import org.slf4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;

interface HttpConnection {

    HttpClient getHttpClient();

    String getBaseServerUrl();

    Logger getLogger();

    default HttpRequest createGetRequest(String endpoint) {
        return HttpRequest.newBuilder()
                .uri(URI.create(getBaseServerUrl() + endpoint))
                .GET()
                .timeout(Duration.ofSeconds(6))
                .build();
    }

    default HttpRequest createPostRequest(String endpoint) {
        return HttpRequest.newBuilder()
                .uri(URI.create(getBaseServerUrl() + endpoint))
                .POST(HttpRequest.BodyPublishers.noBody())
                .timeout(Duration.ofSeconds(6))
                .build();
    }

    default HttpRequest createJsonPostRequest(String endpoint, Object body) {
        String serializedBody = DataProcessor.jsonSerializeObject(body);
        return HttpRequest.newBuilder()
                .uri(URI.create(getBaseServerUrl() + endpoint))
                .POST(HttpRequest.BodyPublishers.ofString(serializedBody))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(6))
                .build();
    }

    default HttpRequest createJsonPatchRequest(String endpoint, Object body) {
        String serializedBody = DataProcessor.jsonSerializeObject(body);
        return HttpRequest.newBuilder()
                .uri(URI.create(getBaseServerUrl() + endpoint))
                .method("PATCH", HttpRequest.BodyPublishers.ofString(serializedBody))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(6))
                .build();
    }

}

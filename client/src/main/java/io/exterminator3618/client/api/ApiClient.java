package io.exterminator3618.client.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.exterminator3618.client.Exterminator3618;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;


public class ApiClient implements FriendsApi, UserApi {

    private static String baseServerUrl;

    protected final HttpClient httpClient;
    protected final JsonMapper jsonMapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
            .build();

    private ApiClient(HttpClient httpClient) throws IOException {
        this.httpClient = httpClient;
    }

    public static ApiClient create(Exterminator3618 game) throws IOException {
        HttpCookie authCookie = new HttpCookie("auth", game.getPreferences().getString("auth"));
        authCookie.setDomain(baseServerUrl);
        authCookie.setPath("/");
        authCookie.setHttpOnly(true);
        CookieManager cookieManager = new CookieManager();
        cookieManager.getCookieStore().add(URI.create(baseServerUrl), authCookie);
        HttpClient client = HttpClient.newBuilder()
                .cookieHandler(cookieManager)
                .build();
        return new ApiClient(client);
    }

    public static ApiClient login(Exterminator3618 game, String username, String password) throws IOException {
        HttpClient client = HttpClient.newHttpClient();
        return null;
    }

    public static ApiClient register(Exterminator3618 game, String name, String username, String password) throws IOException {
        return null;
    }

    protected static void setBaseServerUrl(String baseServerUrl) {
        if (!baseServerUrl.startsWith("https://") || !baseServerUrl.startsWith("http://")) {
            throw new IllegalArgumentException("Invalid base server URL");
        }
        ApiClient.baseServerUrl = baseServerUrl;
    }

    @Override
    public HttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public String getBaseServerUrl() {
        return baseServerUrl;
    }

    @Override
    public String jsonSerializeObject(Object o) {
        try {
            return jsonMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}

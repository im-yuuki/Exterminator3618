package io.exterminator3618.client.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.exterminator3618.client.Exterminator3618;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;


public class ApiClient implements FriendsApi, UserApi {

    protected final Exterminator3618 game;
    protected final UserInfo userInfo = new UserInfo();
    protected final HttpClient httpClient;
    protected final JsonMapper jsonMapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
            .build();

    private ApiClient(Exterminator3618 game, HttpClient httpClient) throws IOException {
        this.game = game;
        this.httpClient = httpClient;
    }

    public static ApiClient create(Exterminator3618 game) throws IOException {
        CookieManager cookieManager = new CookieManager();
        HttpClient httpClient = HttpClient.newBuilder()
                .cookieHandler(cookieManager)
                .build();
        ApiClient client = new ApiClient(game, httpClient);
        HttpCookie authCookie = new HttpCookie("auth", game.getPreferences().getString("auth"));
        authCookie.setDomain(client.getBaseServerUrl());
        authCookie.setPath("/");
        authCookie.setHttpOnly(true);
        cookieManager.getCookieStore().add(URI.create(client.getBaseServerUrl()), authCookie);
        return client;
    }

    public static ApiClient login(Exterminator3618 game, String username, String password) throws IOException {
        HttpClient httpClient = HttpClient.newHttpClient();
        ApiClient client = new ApiClient(game, httpClient);
        HttpRequest req = client.createJsonPostRequest("/api/user/login", new Object() {
            public final String usernameField = username;
            public final String passwordField = password;
        });
        return client;
    }

    public static ApiClient register(Exterminator3618 game, String name, String username, String password) throws IOException {
        HttpClient httpClient = HttpClient.newHttpClient();
        ApiClient client = new ApiClient(game, httpClient);
        return client;
    }

    @Override
    public String getBaseServerUrl() {
        return game.getPreferences().getString("server_url", "http://localhost:36018/api");
    }

    @Override
    public HttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public String jsonSerializeObject(Object o) {
        try {
            return jsonMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public UserInfo internalUserInfoObject() {
        return userInfo;
    }

}

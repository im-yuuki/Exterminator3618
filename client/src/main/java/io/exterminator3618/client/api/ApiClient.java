package io.exterminator3618.client.api;

import io.exterminator3618.client.Exterminator3618;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class ApiClient implements FriendsApi, MatchApi, UserApi {

    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);

    protected final Exterminator3618 game;
    protected final HttpClient httpClient;

    protected final UserInfo userInfo = new UserInfo();
    protected final ArrayList<UserInfo> friendsList = new ArrayList<>();

    private ApiClient(Exterminator3618 game, HttpClient httpClient) {
        this.game = game;
        this.httpClient = httpClient;
    }

    public static ApiClient create(Exterminator3618 game) throws IOException {
        CookieManager cookieManager = new CookieManager();
        HttpClient httpClient = createHttpClient(cookieManager);
        ApiClient client = new ApiClient(game, httpClient);
        HttpCookie authCookie = new HttpCookie("auth", game.getPreferences().getString("auth"));
        authCookie.setDomain(client.getBaseServerUrl());
        authCookie.setPath("/");
        authCookie.setHttpOnly(true);
        cookieManager.getCookieStore().add(URI.create(client.getBaseServerUrl()), authCookie);
        if (!client.fetchUserInfo()) {
            throw new IOException("Failed to fetch user info");
        } else {
            logger.info("Authenticated as {} successfully", client.getUserInfo().getUsername());
        }
        return client;
    }

    public static ApiClient login(Exterminator3618 game, String loginUsername, String loginPassword) throws IOException, InterruptedException {
        HttpClient httpClient = createHttpClient(null);
        ApiClient client = new ApiClient(game, httpClient);
        HttpRequest req = client.createJsonPostRequest("/account/login", new Object() {
            public final String username = loginUsername;
            public final String password = loginPassword;
        });
        HttpResponse<OperationResponse> res = httpClient.send(req, DataProcessor.getOperationResponseHandler());
        if (res.statusCode() != 200) {
            throw new IOException("Login failed with status code: " + res.statusCode());
        } else if (!res.body().isSuccess()) {
            throw new IOException("Login failed: " + res.body().getMessage());
        }
        logger.info("Logged in as {} successfully", loginUsername);
        if (!client.fetchUserInfo()) {
            throw new IOException("Failed to fetch user info after login");
        }
        return client;
    }

    public static ApiClient register(Exterminator3618 game, String registerName, String registerUsername, String registerPassword) throws IOException, InterruptedException {
        HttpClient httpClient = createHttpClient(null);
        ApiClient client = new ApiClient(game, httpClient);
        HttpRequest req = client.createJsonPostRequest("/account/register", new Object() {
            public final String name = registerName;
            public final String username = registerUsername;
            public final String password = registerPassword;
        });
        HttpResponse<OperationResponse> res = httpClient.send(req, DataProcessor.getOperationResponseHandler());
        if (res.statusCode() != 200) {
            throw new IOException("Registration failed with status code: " + res.statusCode());
        } else if (!res.body().isSuccess()) {
            throw new IOException("Registration failed: " + res.body().getMessage());
        }
        logger.info("Registered account {} successfully", registerUsername);
        if (!client.fetchUserInfo()) {
            throw new IOException("Failed to fetch user info after registration");
        }
        return client;
    }

    @Override
    public String getBaseServerUrl() {
        // return "http://localhost:36018/api"; // mock server URL
        return game.getPreferences().getString("server_url", "http://localhost:36018/api");
    }

    @Override
    public HttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public UserInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    public String exportAuthToken() {
        AtomicReference<String> authToken = new AtomicReference<>();
        getHttpClient().cookieHandler().ifPresent(cookieHandler -> {
            if (cookieHandler instanceof CookieManager cookieManager) {
                for (HttpCookie cookie : cookieManager.getCookieStore().getCookies()) {
                    if (cookie.getName().equals("auth")) {
                        authToken.set(cookie.getValue());
                        break;
                    }
                }
            }
        });
        return authToken.get();
    }

    public void saveAuthToken() {
        String authToken = exportAuthToken();
        if (authToken != null) {
            logger.info("Saving auth token to preferences");
            game.getPreferences().putString("auth", authToken);
        } else {
            logger.warn("No auth token found, removing from preferences");
            game.getPreferences().remove("auth");
        }
    }

    private static HttpClient createHttpClient(CookieManager cookieManager) {
        CookieManager useCookieManager = cookieManager;
        if (useCookieManager == null) {
            useCookieManager = new CookieManager();
        }
        useCookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        return HttpClient.newBuilder()
                .cookieHandler(useCookieManager)
                .connectTimeout(Duration.ofSeconds(6))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

}

package io.exterminator3618.client.api;

import java.io.IOException;
import java.net.http.HttpRequest;

public interface UserApi extends HttpConnection {

    default void getUserInfo() {

    }

    default void saveUserInfo() {

    }

    default void changePassword(String currentPassword, String setPassword) {
        HttpRequest req = createJsonPostRequest("/api/user/changePassword", new Object() {
            public final String password = currentPassword;
            public final String newPassword = setPassword;
        });
    }

    default void logout() {
        HttpRequest req = createGetRequest("/api/user/logout");
        try {
            getHttpClient().send(req, (ignored) -> null);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

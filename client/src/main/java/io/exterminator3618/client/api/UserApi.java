package io.exterminator3618.client.api;

import io.exterminator3618.client.utils.JsonResponseHandler;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface UserApi extends HttpConnection {

    default void getUserInfo() {
        HttpRequest req = createGetRequest("/user/info");
        getHttpClient().sendAsync(req, JsonResponseHandler.jsonMapHandler());
    }

    default void saveUserInfo() {
        HttpRequest req = createJsonPatchRequest("/user/info", new Object() {
            public final String name = internalUserInfoObject().getName();
            public final String username = internalUserInfoObject().getUsername();
            public final boolean invisibleMode = internalUserInfoObject().isInvisibleMode();
        });
    }

    default void changePassword(String currentPassword, String setPassword) {
        HttpRequest req = createJsonPostRequest("/user/changePassword", new Object() {
            public final String password = currentPassword;
            public final String newPassword = setPassword;
        });
    }

    default void logout() {
        HttpRequest req = createGetRequest("/user/logout");
        try {
            getHttpClient().send(req, HttpResponse.BodyHandlers.discarding());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    UserInfo internalUserInfoObject();

}

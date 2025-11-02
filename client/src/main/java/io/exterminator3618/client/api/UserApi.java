package io.exterminator3618.client.api;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface UserApi extends HttpConnection {

    default boolean fetchUserInfo() {
        HttpRequest req = createGetRequest("/user/info");
        try {
            HttpResponse<JsonResponse> res = getHttpClient().send(req, DataProcessor.getJsonResponseHandler());
            if (res.statusCode() == 200) {
                UserInfo info = getUserInfo();
                info.setName((String) res.body().get("name"));
                info.setUsername((String) res.body().get("username"));
                info.setInvisibleMode((Boolean) res.body().get("invisibleMode"));
                info.setAverageScore((Integer) res.body().get("averageScore"));
                info.setBestScore((Integer) res.body().get("bestScore"));
                info.setTotalGamesPlayed((Integer) res.body().get("totalGamesPlayed"));
                return true;
            } else {
                getLogger().error("Fetch user info failed with status code {}", res.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            getLogger().error("Fetch user info failed", e);
        }
        return false;
    }

    default boolean saveUserInfo() {
        HttpRequest req = createJsonPatchRequest("/user/info", new Object() {
            public final String name = getUserInfo().getName();
            public final String username = getUserInfo().getUsername();
            public final boolean invisibleMode = getUserInfo().isInvisibleMode();
        });
        try {
            HttpResponse<OperationResponse> res = getHttpClient().send(req, DataProcessor.getOperationResponseHandler());
            if (res.statusCode() != 200) {
                getLogger().error("Save user info failed with status code {}", res.statusCode());
            } else if (!res.body().isSuccess()) {
                getLogger().error("Save user info failed: {}", res.body().getMessage());
            } else {
                getLogger().info("User info saved successfully");
                return true;
            }
        } catch (IOException | InterruptedException e) {
            getLogger().error("Save user info failed", e);
        }
        return false;
    }

    default boolean changePassword(String currentPassword, String setPassword) {
        HttpRequest req = createJsonPostRequest("/user/changePassword", new Object() {
            public final String password = currentPassword;
            public final String newPassword = setPassword;
        });
        try {
            HttpResponse<OperationResponse> res = getHttpClient().send(req, DataProcessor.getOperationResponseHandler());
            if (res.statusCode() != 200) {
                getLogger().error("Change password failed with status code {}", res.statusCode());
            } else if (!res.body().isSuccess()) {
                getLogger().error("Change password failed: {}", res.body().getMessage());
            } else {
                getLogger().info("Password changed successfully");
                return true;
            }
        } catch (IOException | InterruptedException e) {
            getLogger().error("Change password failed", e);
        }
        return false;
    }

    default boolean logout() {
        HttpRequest req = createGetRequest("/user/logout");
        try {
            HttpResponse<Void> res = getHttpClient().send(req, HttpResponse.BodyHandlers.discarding());
            if (res.statusCode() != 200) {
                getLogger().warn("Logout with status code {}", res.statusCode());
            }
            return true;
        } catch (IOException | InterruptedException e) {
            getLogger().error("Logout failed", e);
            return false;
        }
    }

    UserInfo getUserInfo();

}

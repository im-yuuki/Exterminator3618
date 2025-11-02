package io.exterminator3618.client.api;

import org.slf4j.Logger;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

interface FriendsApi extends HttpConnection {

    default boolean getFriendsList() {
        HttpRequest req = createGetRequest("/friends/list");
        return false;
    }

    default boolean addFriend(String friendUsername) {
        HttpRequest req = createJsonPostRequest("/friends/add", new Object() {
            public final String friendAccountUsername = friendUsername;
        });
        try {
            HttpResponse<OperationResponse> res = getHttpClient().send(req, DataProcessor.getOperationResponseHandler());
            if (res.statusCode() != 200) {
                getLogger().error("Add friend with {} failed with status code {}", friendUsername, res.statusCode());
            } else if (!res.body().isSuccess()) {
                getLogger().error("Add friend with {} failed: {}", friendUsername, res.body().getMessage());
            } else {
                getLogger().info("Added {} as friend", friendUsername);
                return true;
            }
        } catch (IOException | InterruptedException e) {
            getLogger().error("Add friend with {} failed", friendUsername, e);
        }
        return false;
    }

    default boolean removeFriend(String friendUsername) throws IOException, InterruptedException {
        HttpRequest req = createJsonPostRequest("/friends/remove", new Object() {
            public final String friendAccountUsername = friendUsername;
        });
        try {
            HttpResponse<OperationResponse> res = getHttpClient().send(req, DataProcessor.getOperationResponseHandler());
            if (res.statusCode() != 200) {
                getLogger().error("Remove friend {} failed with status code {}", friendUsername, res.statusCode());
            } else if (!res.body().isSuccess()) {
                getLogger().error("Remove friend {} failed: {}", friendUsername, res.body().getMessage());
            } else {
                getLogger().info("Removed friend {}", friendUsername);
                return true;
            }
        } catch (IOException | InterruptedException e) {
            getLogger().error("Remove friend {} failed", friendUsername, e);
        }
        HttpResponse<JsonResponse> res = getHttpClient().send(req, DataProcessor.getJsonResponseHandler());
        return false;
    }

}

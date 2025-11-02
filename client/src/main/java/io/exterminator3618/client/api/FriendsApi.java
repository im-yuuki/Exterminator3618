package io.exterminator3618.client.api;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public interface FriendsApi extends HttpConnection {

    default boolean fetchFriendsList() {
        HttpRequest req = createGetRequest("/friends/list");
        try {
            HttpResponse<FriendListResponse> res = getHttpClient().send(req, DataProcessor.getFriendListResponseHandler());
            if (res.statusCode() != 200) {
                getLogger().error("Get friends list failed with status code {}", res.statusCode());
            } else {
                ArrayList<UserInfo> friends = getFriendsList();
                friends.clear();
                friends.addAll(res.body());
                return true;
            }
        } catch (IOException | InterruptedException e) {
            getLogger().error("Get friends list failed", e);
        }
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

    default boolean removeFriend(String friendUsername) {
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
        return false;
    }

    ArrayList<UserInfo> getFriendsList();

}

package io.exterminator3618.client.api;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

interface FriendsApi extends HttpConnection {

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
                friends.sort((a, b) -> {
                    int aPoint = 0, bPoint = 0;
                    aPoint += a.isOnline() ? 1 : 0;
                    bPoint += b.isOnline() ? 1 : 0;
                    aPoint += a.isInMatch() ? 1 : 0;
                    bPoint += b.isInMatch() ? 1 : 0;
                    if (aPoint == bPoint) {
                        return a.getUsername().compareToIgnoreCase(b.getUsername());
                    } else {
                        return Integer.compare(bPoint, aPoint);
                    }
                });
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

    ArrayList<UserInfo> getFriendsList();

}

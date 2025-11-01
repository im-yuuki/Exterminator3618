package io.exterminator3618.client.api;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

interface FriendsApi extends HttpConnection {

    default List<UserInfo> getFriendsList() {
        HttpRequest req = createGetRequest("/api/friends/list");
        return new ArrayList<>();
    }

    default void addFriend(String friendUsername) {
        HttpRequest req = createJsonPostRequest("/api/friends/add", new Object() {
            public final String friendAccountUsername = friendUsername;
        });
    }

    default void removeFriend(String friendUsername) {

    }

}

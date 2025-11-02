package io.exterminator3618.client.api;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

public interface MatchApi extends HttpConnection {

    default boolean fetchStatus() {
        HttpRequest req = createGetRequest("/match/status");
        try {
            var response = getHttpClient().send(req, DataProcessor.getJsonResponseHandler());
            if (response.statusCode() == 200) {
                setInMatch((Boolean) response.body().get("inMatch"));
                ArrayList<Object> invites = (ArrayList<Object>) response.body().get("matchInvite");
                getMatchInvites().clear();
                for (Object invite : invites) {
                    MatchInvite mi = DataProcessor.jsonDeserializeObject(
                            DataProcessor.jsonSerializeObject(invite),
                            MatchInvite.class
                    );
                    getMatchInvites().add(mi);
                }
                return true;
            } else {
                getLogger().error("Failed to fetch match status. HTTP Status: {}", response.statusCode());
            }
        } catch (Exception e) {
            getLogger().error("Exception while fetching match status", e);
        }
        return false;
    }

    default boolean inviteFriendToMatch(String friendUsername) {
        HttpRequest req = createJsonPostRequest("/match/invite", new Object() {
            public final String friendAccountUsername = friendUsername;
        });
        try {
            var res = getHttpClient().send(req, DataProcessor.getOperationResponseHandler());
            if (res.statusCode() != 200) {
                getLogger().error("Invite {} failed with status code {}", friendUsername, res.statusCode());
            } else if (!res.body().isSuccess()) {
                getLogger().error("Invite {} failed: {}", friendUsername, res.body().getMessage());
            } else {
                getLogger().info("Invited {}", friendUsername);
                return true;
            }
        } catch (Exception e) {
            getLogger().error("Exception while inviting friend {} to match", friendUsername, e);
        }
        return false;
    }

    default boolean acceptInvite(String friendUsername) {
        HttpRequest req = createJsonPostRequest("/match/acceptInvite", new Object() {
            public final String friendAccountUsername = friendUsername;
        });
        try {
            var res = getHttpClient().send(req, DataProcessor.getOperationResponseHandler());
            if (res.statusCode() != 200) {
                getLogger().error("Accept invite from {} failed with status code {}", friendUsername, res.statusCode());
            } else if (!res.body().isSuccess()) {
                getLogger().error("Accept invite from {} failed: {}", friendUsername, res.body().getMessage());
            } else {
                getLogger().info("Accepted invite from {}", friendUsername);
                return true;
            }
        } catch (Exception e) {
            getLogger().error("Exception while accepting invite from {}", friendUsername, e);
        }
        return false;
    }

    default boolean joinQueue() {
        HttpRequest req = createPostRequest("/match/joinQueue");
        try {
            var res = getHttpClient().send(req, DataProcessor.getOperationResponseHandler());
            if (res.statusCode() != 200) {
                getLogger().error("Join queue failed with status code {}", res.statusCode());
            } else if (!res.body().isSuccess()) {
                getLogger().error("Join queue failed: {}", res.body().getMessage());
            } else {
                getLogger().info("Joined match queue");
                return true;
            }
        } catch (Exception e) {
            getLogger().error("Exception while joining match queue", e);
        }
        return false;
    }

    default boolean leaveQueue() {
        HttpRequest req = createPostRequest("/match/leaveQueue");
        try {
            var res = getHttpClient().send(req, DataProcessor.getOperationResponseHandler());
            if (res.statusCode() != 200) {
                getLogger().error("Leave queue failed with status code {}", res.statusCode());
            } else if (!res.body().isSuccess()) {
                getLogger().error("Leave queue failed: {}", res.body().getMessage());
            } else {
                getLogger().info("Left match queue");
                return true;
            }
        } catch (Exception e) {
            getLogger().error("Exception while leaving match queue", e);
        }
        return false;
    }

    default boolean fetchRoomStatus() {
        HttpRequest req = createGetRequest("/match/roomStatus");
        try {
            var response = getHttpClient().send(req, DataProcessor.getRoomStatusResponseHandler());
            if (response.statusCode() == 200) {
                setRoomStatus(response.body());
                return true;
            } else {
                getLogger().error("Failed to fetch match room status. HTTP Status: {}", response.statusCode());
            }
        } catch (Exception e) {
            getLogger().error("Exception while fetching match room status", e);
        }
        return false;
    }

    default boolean pushGameEvents(List<String> data) {
        HttpRequest req = createJsonPostRequest("/match/pushGameEvents", new Object() {
            public final String[] list = data.toArray(new String[] {});
        });
        try {
            var res = getHttpClient().send(req, DataProcessor.getOperationResponseHandler());
            if (res.statusCode() != 200) {
                getLogger().error("Push game events failed with status code {}", res.statusCode());
            } else if (!res.body().isSuccess()) {
                getLogger().error("Push game events failed: {}", res.body().getMessage());
            } else {
                getLogger().info("Pushed {} game events", data.size());
                return true;
            }
        } catch (Exception e) {
            getLogger().error("Exception while pushing game events", e);
        }
        return false;
    }

    default boolean leaveRoom() {
        HttpRequest req = createPostRequest("/match/leaveRoom");
        try {
            var res = getHttpClient().send(req, DataProcessor.getOperationResponseHandler());
            if (res.statusCode() != 200) {
                getLogger().error("Leave room failed with status code {}", res.statusCode());
            } else if (!res.body().isSuccess()) {
                getLogger().error("Leave room failed: {}", res.body().getMessage());
            } else {
                getLogger().info("Left match room");
                return true;
            }
        } catch (Exception e) {
            getLogger().error("Exception while leaving match room", e);
        }
        return false;
    }

    boolean isInMatch();
    void setInMatch(boolean inMatch);
    ArrayList<MatchInvite> getMatchInvites();
    RoomStatus getRoomStatus();
    void setRoomStatus(RoomStatus roomStatus);

}

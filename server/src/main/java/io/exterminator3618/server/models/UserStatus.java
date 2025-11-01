package io.exterminator3618.server.models;

public record UserStatus(boolean inMatch, MatchInvite[] matchInvite) {

    public record MatchInvite(String fromPlayerName, String fromPlayerUsername) {

    }

}

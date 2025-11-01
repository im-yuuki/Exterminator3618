package io.exterminator3618.server.utils;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class Room {

    private final String mode = "standard";
    private final String mapCode;
    private final HashMap<Long, RecordUtility> members = new HashMap<>();

    public Room(String mapCode, long... memberIds) {
        this.mapCode = mapCode;
        for (long id : memberIds) {
            members.put(id, new RecordUtility());
        }
    }

    public void setFinished(long memberId) {
        var member = members.get(memberId);
        if (member == null) {
            throw new ForbiddenAction("Member not in room");
        }
        if (member.isFinished()) {
            throw new ForbiddenAction("Game already finished for this member");
        }
        member.setFinished(true);
    }

    public void pushGameEvents(long memberId, String rawGameEventData) {
        var member = members.get(memberId);
        if (member == null) {
            throw new ForbiddenAction("Member not in room");
        }
        if (rawGameEventData == null || rawGameEventData.isEmpty()) {
            throw new IllegalArgumentException("No game event data provided");
        }
        if (member.isFinished()) {
            throw new ForbiddenAction("Game already finished for this member");
        }
        member.logEvent(rawGameEventData);
    }

}

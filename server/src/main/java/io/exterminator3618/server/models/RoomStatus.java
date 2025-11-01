package io.exterminator3618.server.models;

import lombok.Data;
import lombok.NonNull;

@Data
public class RoomStatus {

    private String mode;
    private String mapCode;

    private int totalPlayers;
    private int finishedPlayers;

    @NonNull
    private MemberStatus[] members;

    @Data
    public static class MemberStatus {

        private String playerName;
        private String playerUsername;
        private boolean isFinished;
        private int score;
        private int combo;
        private int maxCombo;
        private int hp;

    }


}

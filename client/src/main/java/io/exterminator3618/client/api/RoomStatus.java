package io.exterminator3618.client.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Collections;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomStatus {

    private String mode;
    private String mapCode;

    private int totalPlayers;
    private int finishedPlayers;

    private ArrayList<MemberStatus> members;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MemberStatus {

        private String playerName;
        private String playerUsername;
        private boolean isFinished;
        private int score;
        private int combo;
        private int maxCombo;
        private int hp;

        public String getPlayerName() {
            return playerName;
        }

        public void setPlayerName(String playerName) {
            this.playerName = playerName;
        }

        public String getPlayerUsername() {
            return playerUsername;
        }

        public void setPlayerUsername(String playerUsername) {
            this.playerUsername = playerUsername;
        }

        public boolean isFinished() {
            return isFinished;
        }

        public void setFinished(boolean finished) {
            isFinished = finished;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getCombo() {
            return combo;
        }

        public void setCombo(int combo) {
            this.combo = combo;
        }

        public int getMaxCombo() {
            return maxCombo;
        }

        public void setMaxCombo(int maxCombo) {
            this.maxCombo = maxCombo;
        }

        public int getHp() {
            return hp;
        }

        public void setHp(int hp) {
            this.hp = hp;
        }

    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMapCode() {
        return mapCode;
    }

    public void setMapCode(String mapCode) {
        this.mapCode = mapCode;
    }

    public int getFinishedPlayers() {
        return finishedPlayers;
    }

    public void setFinishedPlayers(int finishedPlayers) {
        this.finishedPlayers = finishedPlayers;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }

    public void setTotalPlayers(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public ArrayList<MemberStatus> getMembersList() {
        return members;
    }

    public void setMembers(ArrayList<MemberStatus> members) {
        this.members = members;
    }

}


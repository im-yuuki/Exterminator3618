package io.exterminator3618.client.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo {

    private String name;
    private String username;
    private boolean invisibleMode;

    private int averageScore = 0;
    private int bestScore = 0;
    private int totalGamesPlayed = 0;

    // Friend status only
    private boolean isOnline = false;
    private boolean isInMatch = false;

    public UserInfo() {
    }

    public UserInfo(UserInfo other) {
        this.name = other.name;
        this.username = other.username;
        this.invisibleMode = other.invisibleMode;
        this.averageScore = other.averageScore;
        this.bestScore = other.bestScore;
        this.totalGamesPlayed = other.totalGamesPlayed;
        this.isOnline = other.isOnline;
        this.isInMatch = other.isInMatch;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isInvisibleMode() {
        return invisibleMode;
    }

    public void setInvisibleMode(boolean invisibleMode) {
        this.invisibleMode = invisibleMode;
    }

    public int getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(int averageScore) {
        this.averageScore = averageScore;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public void setTotalGamesPlayed(int totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isInMatch() {
        return isInMatch;
    }

    public void setInMatch(boolean inMatch) {
        isInMatch = inMatch;
    }

}

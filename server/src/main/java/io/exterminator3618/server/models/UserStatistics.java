package io.exterminator3618.server.models;

import lombok.Data;

@Data
public class UserStatistics {

    private int averageScore = 0;
    private int bestScore = 0;
    private int totalGamesPlayed = 0;

    public UserStatistics(double averageScore, int bestScore, long totalGamesPlayed) {
        this.averageScore = (int) averageScore;
        this.bestScore = (int) bestScore;
        this.totalGamesPlayed = (int) totalGamesPlayed;
    }

    public UserStatistics(UserStatistics statistics) {
        this.averageScore = statistics.averageScore;
        this.bestScore = statistics.bestScore;
        this.totalGamesPlayed = statistics.totalGamesPlayed;
    }

}

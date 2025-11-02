package io.exterminator3618.server.models;

import lombok.Data;

@Data
public class UserStatistics {

    private int averageScore = 0;
    private int bestScore = 0;
    private int totalGamesPlayed = 0;

    public UserStatistics(Double averageScore, Integer bestScore, Long totalGamesPlayed) {
        if (averageScore != null) {
            this.averageScore = averageScore.intValue();
        }
        if (bestScore != null) {
            this.bestScore = bestScore;
        }
        if (totalGamesPlayed != null) {
            this.totalGamesPlayed = totalGamesPlayed.intValue();
        }
    }

    public UserStatistics(int averageScore, int bestScore, int totalGamesPlayed) {
        this.averageScore = averageScore;
        this.bestScore = bestScore;
        this.totalGamesPlayed = totalGamesPlayed;
    }

    public UserStatistics(UserStatistics statistics) {
        this.averageScore = statistics.averageScore;
        this.bestScore = statistics.bestScore;
        this.totalGamesPlayed = statistics.totalGamesPlayed;
    }

}

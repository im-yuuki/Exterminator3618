package io.exterminator3618.server.models;

import lombok.Data;

@Data
public class UserStatistics {

    private int averageScore = 0;
    private int bestScore = 0;
    private int totalGamesPlayed = 0;

}

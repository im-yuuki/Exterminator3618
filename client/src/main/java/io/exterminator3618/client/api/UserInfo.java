package io.exterminator3618.client.api;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserInfo {

    private String name;
    private String username;
    private boolean invisibleMode;
    private LocalDateTime accountCreatedAt;

    private int averageScore = 0;
    private int bestScore = 0;
    private int totalGamesPlayed = 0;

}

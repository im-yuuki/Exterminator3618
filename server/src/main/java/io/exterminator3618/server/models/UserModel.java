package io.exterminator3618.server.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserModel {

    private String name;
    private String username;
    private Boolean invisibleMode;

    // ignore in user update
    private LocalDateTime accountCreatedAt;

    // statistics
    private int averageScore = 0;
    private int bestScore = 0;
    private int totalGamesPlayed = 0;

}

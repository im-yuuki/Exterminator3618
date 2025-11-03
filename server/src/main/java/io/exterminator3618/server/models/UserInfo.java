package io.exterminator3618.server.models;

import io.exterminator3618.server.data.Account;
import lombok.*;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public final class UserInfo extends UserStatistics {

    private String name;
    private String username;
    private boolean invisibleMode;
    private LocalDateTime accountCreatedAt;

    public UserInfo(String name, String username, boolean invisibleMode, LocalDateTime accountCreatedAt, UserStatistics statistics) {
        super(statistics);
        this.name = name;
        this.username = username;
        this.invisibleMode = invisibleMode;
        this.accountCreatedAt = accountCreatedAt;
    }

    public UserInfo(Account account, UserStatistics statistics) {
        super(statistics);
        name = account.getName();
        username = account.getUsername();
        invisibleMode = account.isInvisibleMode();
        accountCreatedAt = account.getCreatedAt();
    }

    private boolean isOnline = false;
    private boolean isInMatch = false;

}

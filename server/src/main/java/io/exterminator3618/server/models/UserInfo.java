package io.exterminator3618.server.models;

import io.exterminator3618.server.data.Account;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class UserInfo extends UserStatistics {

    private final String name;
    private final String username;
    private final boolean invisibleMode;
    private final LocalDateTime accountCreatedAt;

    public UserInfo(Account account) {
        name = account.getName();
        username = account.getUsername();
        invisibleMode = account.isInvisibleMode();
        accountCreatedAt = account.getCreatedAt();
    }

    private boolean isOnline = false;
    private boolean isInMatch = false;

}

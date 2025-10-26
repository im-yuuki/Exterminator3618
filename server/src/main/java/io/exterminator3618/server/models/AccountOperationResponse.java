package io.exterminator3618.server.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountOperationResponse {

    private final boolean success;
    private final String message;

    private Long accountId;
    private String accountName;
    private LocalDateTime lastLoginAt;
    private String sessionToken;

    private final LocalDateTime timestamp = LocalDateTime.now();

}

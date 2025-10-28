package io.exterminator3618.server.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class OperationWithSessionTokenResponse extends OperationResponse {

    public OperationWithSessionTokenResponse(boolean success, String message) {
        super(success, message);
    }

    private Long accountId;
    private String accountName;
    private LocalDateTime lastLoginAt;
    private String sessionToken;

}

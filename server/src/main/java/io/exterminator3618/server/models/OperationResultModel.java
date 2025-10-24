package io.exterminator3618.server.models;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OperationResultModel {

    private final boolean success;
    private final String message;
    private final LocalDateTime timestamp;

}

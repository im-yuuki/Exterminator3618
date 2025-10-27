package io.exterminator3618.server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationResponse {

    private boolean success;
    private String message;

    private final LocalDateTime timestamp = LocalDateTime.now();

}

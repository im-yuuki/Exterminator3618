package io.exterminator3618.server.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SystemStatusResponse {

    private final Long serverUptime;

    private final LocalDateTime timestamp = LocalDateTime.now();

}

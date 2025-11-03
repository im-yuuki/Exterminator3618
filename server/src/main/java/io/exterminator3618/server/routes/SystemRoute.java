package io.exterminator3618.server.routes;

import io.exterminator3618.server.models.SystemStatusResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestController
@RequestMapping("/api/system")
public class SystemRoute {

    private static final LocalDateTime serverStartTime = LocalDateTime.now();

    @GetMapping("/status")
    public SystemStatusResponse getStatus() {
        return new SystemStatusResponse(
                LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - serverStartTime.toEpochSecond(ZoneOffset.UTC)
        );
    }

}

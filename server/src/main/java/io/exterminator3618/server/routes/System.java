package io.exterminator3618.server.routes;

import io.exterminator3618.server.models.SystemStatusModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/system")
public class System {

    @GetMapping("/status")
    public SystemStatusModel getStatus() {
        return new SystemStatusModel(LocalDateTime.now());
    }

}

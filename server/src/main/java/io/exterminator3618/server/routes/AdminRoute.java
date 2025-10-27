package io.exterminator3618.server.routes;

import io.exterminator3618.server.data.Ban;
import io.exterminator3618.server.models.OperationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminRoute {

    @GetMapping("/bans")
    public List<Ban> getBans() {
        // TODO: Implement get bans
        return new ArrayList<>();
    }

    @PostMapping("/ban")
    public OperationResponse banAccount() {
        // TODO: Implement ban account
        return new OperationResponse(false, "Feature not implemented");
    }

}

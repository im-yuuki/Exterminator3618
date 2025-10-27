package io.exterminator3618.server.routes;

import io.exterminator3618.server.models.LoginInfoModel;
import io.exterminator3618.server.models.OperationResultModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/account")
public class Account {

    @PostMapping("/login")
    public OperationResultModel login(@RequestBody LoginInfoModel loginInfo) {
        return new OperationResultModel(false, "Not implemented", LocalDateTime.now());
    }

    @PostMapping("/register")
    public OperationResultModel register() {
        return new OperationResultModel(false, "Not implemented", LocalDateTime.now());

    }

    @PostMapping("/recover")
    public OperationResultModel recover() {
        return null; // TODO: implement
    }

}

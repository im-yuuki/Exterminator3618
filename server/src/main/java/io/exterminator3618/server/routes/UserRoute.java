package io.exterminator3618.server.routes;

import io.exterminator3618.server.data.Account;
import io.exterminator3618.server.models.OperationResponse;
import io.exterminator3618.server.models.UserModel;
import io.exterminator3618.server.repositories.AccountRepository;
import io.exterminator3618.server.services.SessionService;
import io.exterminator3618.server.utils.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Slf4j
@RequiredArgsConstructor
public class UserRoute {

    private final AccountRepository accountRepository;
    private final SessionService sessionService;

    @GetMapping("/info")
    public UserModel getUserInfo(@RequestAttribute(name = "userId") Long userId) {
        Account account = accountRepository.findById(userId).orElse(null);
        if (account == null) {
            throw new InvalidRequestException("Account not found");
        }
        var userModel = new UserModel();
        userModel.setName(account.getName());
        userModel.setUsername(account.getUsername());
        userModel.setAccountCreatedAt(account.getCreatedAt());
        userModel.setInvisibleMode(account.isInvisibleMode());
        return userModel;
    }

    @PatchMapping("/info")
    public OperationResponse updateUserInfo(@RequestAttribute(name = "userId") Long userId, @RequestBody UserModel data) {
        Account account = accountRepository.findById(userId).orElse(null);
        if (account == null) {
            throw new InvalidRequestException("Account not found");
        }
        // update username
        if (data.getUsername() != null && !data.getUsername().isEmpty()) {
            var existingAccount = accountRepository.findAccountByUsername(data.getUsername());
            if (existingAccount != null) {
                return new OperationResponse(false, "Username already taken");
            }
            account.setUsername(data.getUsername());
        }
        // update name
        if (data.getName() != null && !data.getName().isEmpty()) {
            account.setName(data.getName());
        }
        // update invisible mode
        if (data.getInvisibleMode() != null) {
            account.setInvisibleMode(data.getInvisibleMode());
        }
        accountRepository.save(account);
        return new OperationResponse(true, "User info updated successfully");
    }

    @PostMapping("/logout")
    public OperationResponse logout(@RequestHeader Map<String, String> headers) {
        String authorization = headers.get("Authorization");
        if (authorization == null || authorization.isEmpty()) {
            throw new InvalidRequestException("Authorization header is missing");
        }
        sessionService.invalidateSessionToken(authorization);
        return new OperationResponse(true, "Logout successful");
    }

}

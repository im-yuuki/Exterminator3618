package io.exterminator3618.server.routes;

import io.exterminator3618.server.data.Account;
import io.exterminator3618.server.models.ChangePasswordRequest;
import io.exterminator3618.server.models.OperationResponse;
import io.exterminator3618.server.models.UserInfo;
import io.exterminator3618.server.repositories.AccountRepository;
import io.exterminator3618.server.services.MatchFindService;
import io.exterminator3618.server.services.SessionService;
import io.exterminator3618.server.utils.InvalidRequestException;
import io.exterminator3618.server.utils.PasswordHash;
import io.exterminator3618.server.utils.UsernameRequirementsCheck;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    private final MatchFindService matchFindService;

    @GetMapping("/alive")
    public void userAlive(@RequestAttribute(name = "userId") Long userId) {
        log.debug("Received online ping from user ID: {}", userId);
        matchFindService.setOnline(userId);
    }

    @GetMapping("/info")
    public UserInfo getUserInfo(@RequestAttribute(name = "userId") Long userId) {
        Account account = accountRepository.findById(userId).orElse(null);
        if (account == null) {
            throw new InvalidRequestException("Account not found");
        }
        return new UserInfo(account);
    }

    @PatchMapping("/info")
    public OperationResponse updateUserInfo(@RequestAttribute(name = "userId") Long userId, @RequestBody UserInfo data) {
        Account account = accountRepository.findById(userId).orElse(null);
        if (account == null) {
            throw new InvalidRequestException("Account not found");
        }
        // update username
        if (data.getUsername() != null && !data.getUsername().isEmpty()) {
            if (!UsernameRequirementsCheck.check(data.getUsername())) {
                return new OperationResponse(false, "Username does not meet requirements");
            }
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
        account.setInvisibleMode(data.isInvisibleMode());
        accountRepository.save(account);
        return new OperationResponse(true, "User info updated successfully");
    }

    @PostMapping("/changePassword")
    public OperationResponse changePassword(@RequestAttribute(name = "userId") Long userId, @RequestBody ChangePasswordRequest req) {
        Account account = accountRepository.findById(userId).orElse(null);
        if (account == null) {
            throw new InvalidRequestException("Account not found");
        }
        if (!PasswordHash.verifyPassword(req.password(), account.getPwdHash())) {
            return new OperationResponse(false, "Invalid current password");
        }
        account.setPwdHash(PasswordHash.hashPassword(req.newPassword()));
        accountRepository.save(account);
        return new OperationResponse(true, "Password changed successfully");
    }

    @PostMapping("/logout")
    public OperationResponse logout(@CookieValue(name = "auth", required = false) String authCookie, HttpServletResponse response) {
        if (authCookie != null) {
            sessionService.invalidateSessionToken(authCookie);
        }
        Cookie cookie = new Cookie("auth", "");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return new OperationResponse(true, "Logged out successfully");
    }

}

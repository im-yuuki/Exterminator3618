package io.exterminator3618.server.routes;

import io.exterminator3618.server.data.Account;
import io.exterminator3618.server.data.Ban;
import io.exterminator3618.server.models.*;
import io.exterminator3618.server.repositories.AccountRepository;
import io.exterminator3618.server.services.SessionService;
import io.exterminator3618.server.utils.PasswordHash;
import io.exterminator3618.server.utils.UsernameRequirementsCheck;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/account")
@Slf4j
@RequiredArgsConstructor
public class AccountRoute {

    private final AccountRepository accountRepository;
    private final SessionService sessionService;

    @PostMapping("/login")
    @Transactional
    public OperationResponse login(@RequestBody LoginRequest req, HttpServletResponse response) {
        Account account =  accountRepository.findAccountByUsername(req.username());
        if (account == null) {
            log.debug("Username \"{}\" not found", req.username());
            return new OperationResponse(false, "Account not found");
        } else if (!PasswordHash.verifyPassword(req.password(), account.getPwdHash())) {
            log.debug("Login attempt for \"{}\" failed: invalid password", req.username());
            return new OperationResponse(false, "Invalid password");
        }
        String banMessage = isBannedAccount(account);
        if (banMessage != null) {
            log.debug("Login attempt for \"{}\" failed: {}", req.username(), banMessage);
            return new OperationResponse(false, banMessage);
        }
        // Set session token
        Cookie authCookie = new Cookie("auth", sessionService.generateSessionToken(account.getId()));
        authCookie.setHttpOnly(true);
        authCookie.setPath("/");
        response.addCookie(authCookie);
        // Update last login time
        account.setLastLoginAt(LocalDateTime.now());
        accountRepository.save(account);
        log.debug("User \"{}\" logged in successfully", req.username());
        return new OperationResponse(true, "Login successful");
    }

    @PostMapping("/register")
    public OperationResponse register(@RequestBody RegisterRequest req, HttpServletResponse response) {
        var existingAccount = accountRepository.findAccountByUsername(req.username());
        if (existingAccount != null) {
            return new OperationResponse(false, "Username already taken");
        }
        if (!UsernameRequirementsCheck.check(req.username())) {
            return new OperationResponse(false, "Username does not meet the requirements");
        }
        Account newAccount = new Account();
        newAccount.setUsername(req.username());
        newAccount.setName(req.name());
        newAccount.setPwdHash(PasswordHash.hashPassword(req.password()));
        accountRepository.save(newAccount);
        // Set session token
        Cookie authCookie = new Cookie("auth", sessionService.generateSessionToken(newAccount.getId()));
        authCookie.setHttpOnly(true);
        authCookie.setPath("/");
        response.addCookie(authCookie);
        log.debug("New account registered: \"{}\" ({}), ID: {}", newAccount.getUsername(), newAccount.getName(), newAccount.getId());
        return new OperationResponse(true, "Registration successful");
    }

    @PostMapping("/recover")
    @Deprecated
    public OperationResponse recover(@RequestBody RecoverRequest req) {
        return new OperationResponse(false, "Account recovery not implemented");
    }

    /**
     * Check if the provided account is banned.
     *
     * @param account the account entity
     * @return banned message of the ban which have the longest remaining time, or null if not banned
     */
    private static String isBannedAccount(Account account) {
        var bans = account.getBans();
        Ban longestBan = null;
        for (var ban : bans) {
            if (ban.getValidTo() == null) {
                // this is a permanent ban
                longestBan = ban;
                break;
            }
            if (ban.getValidTo().isAfter(LocalDateTime.now())) {
                if (longestBan == null) {
                    longestBan = ban;
                } else if (ban.getValidTo().isAfter(longestBan.getValidTo())) {
                    longestBan = ban;
                }
            }
        }
        if (longestBan == null) {
            return null;
        }
        StringBuilder banMessage = new StringBuilder("Account is banned");
        if (longestBan.getValidTo() != null) {
            banMessage.append(" until ").append(longestBan.getValidTo().toString());
        } else {
            banMessage.append(" permanently");
        }
        if (longestBan.getReason() != null && !longestBan.getReason().isEmpty()) {
            banMessage.append(". Reason: ").append(longestBan.getReason());
        }
        return banMessage.toString();
    }

}

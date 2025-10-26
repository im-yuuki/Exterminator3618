package io.exterminator3618.server.routes;

import io.exterminator3618.server.data.Account;
import io.exterminator3618.server.models.AccountOperationResponse;
import io.exterminator3618.server.models.LoginRequest;
import io.exterminator3618.server.models.RecoverRequest;
import io.exterminator3618.server.models.RegisterRequest;
import io.exterminator3618.server.repositories.AccountRepository;
import io.exterminator3618.server.utils.PasswordHash;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountRoute {

    private final AccountRepository accountRepository;

    @PostMapping("/login")
    public AccountOperationResponse login(@RequestBody LoginRequest req) {
        Account account =  accountRepository.findAccountByUsername(req.username());
        if (account == null) {
            return new AccountOperationResponse(false, "Account not found");
        } else if (!PasswordHash.verifyPassword(req.password(), account.getPwdHash())) {
            return new AccountOperationResponse(false, "Invalid password");
        }
        var bans = account.getBans();
        for (var ban : bans) {
            if (ban.getValidTo() == null || ban.getValidTo().isAfter(LocalDateTime.now())) {
                StringBuilder banMessage = new StringBuilder("Account is banned");
                if (ban.getValidTo() != null) {
                    banMessage.append(" until ").append(ban.getValidTo().toString());
                }
                if (ban.getReason() != null && !ban.getReason().isEmpty()) {
                    banMessage.append(". Reason: ").append(ban.getReason());
                }
                return new AccountOperationResponse(false, banMessage.toString());
            }
        }
        var res = new AccountOperationResponse(true, "Login successful");
        res.setAccountId(account.getId());
        res.setAccountName(account.getName());
        res.setLastLoginAt(account.getLastLoginAt());
        res.setSessionToken("");
        // Update last login time
        account.setLastLoginAt(LocalDateTime.now());
        accountRepository.save(account);
        return res;
    }

    @PostMapping("/register")
    public AccountOperationResponse register(@RequestBody RegisterRequest req) {
        var existingAccount = accountRepository.findAccountByUsername(req.username());
        if (existingAccount != null) {
            return new AccountOperationResponse(false, "Username already taken");
        }
        // Create new account
        Account newAccount = new Account();
        newAccount.setUsername(req.username());
        newAccount.setName(req.name());
        newAccount.setPwdHash(PasswordHash.hashPassword(req.password()));
        accountRepository.save(newAccount);
        var res = new AccountOperationResponse(true, "Account registered successfully");
        res.setAccountId(newAccount.getId());
        res.setAccountName(newAccount.getName());
        res.setLastLoginAt(newAccount.getLastLoginAt());
        res.setSessionToken("");
        return res;
    }

    @PostMapping("/recover")
    public AccountOperationResponse recover(@RequestBody RecoverRequest req) {
        return new AccountOperationResponse(false, "Account recovery not implemented");
    }

}

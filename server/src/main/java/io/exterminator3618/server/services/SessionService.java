package io.exterminator3618.server.services;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SessionService {

    private final Cache<@NonNull String, Long> activeSessions = Caffeine.newBuilder()
            .expireAfterAccess(3, TimeUnit.DAYS)
            .expireAfterWrite(3, TimeUnit.DAYS)
            .build();

    public String generateSessionToken(long accountId) {
        String token;
        do {
            token = generateRandomTokenString();
        } while (activeSessions.getIfPresent(token) != null);
        activeSessions.put(token, accountId);
        log.debug("Generated session token for account ID {}", accountId);
        return token;
    }

    public Long getSessionAccountId(@NonNull String token) {
        Long accountId = activeSessions.getIfPresent(token);
        if (accountId == null) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        return accountId;
    }

    public void invalidateSessionToken(@NonNull String token) {
        activeSessions.invalidate(token);
    }

    private static final Random random = new Random();
    private static final String tokenChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * Generates a random token string of length 56 using alphanumeric characters.
     *
     * @return The generated token string.
     */
    @NonNull
    private static String generateRandomTokenString() {
        StringBuilder sb = new StringBuilder(56);
        for (int i = 0; i < 56; i++) {
            sb.append(tokenChars.charAt(random.nextInt(tokenChars.length())));
        }
        return sb.toString();
    }

}

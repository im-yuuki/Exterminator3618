package io.exterminator3618.server.services;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MatchService {

    /**
     * Cache to track if a user is currently in a match or online.
     * Value is true if the user is in a match, false if just online, and null if offline/invisible.
     * Entries expire after 2 minutes of inactivity.
     */
    private final Cache<@NonNull Long, Boolean> userInMatchOrOnline = Caffeine.newBuilder()
            .expireAfterWrite(2, TimeUnit.MINUTES)
            .build();


    public Boolean getUserStatus(long accountId) {
        return userInMatchOrOnline.getIfPresent(accountId);
    }

    public void setOnline(long accountId) {
        userInMatchOrOnline.put(accountId, false);
    }

    public void setInMatch(long accountId) {
        userInMatchOrOnline.put(accountId, true);
    }


}

package io.exterminator3618.server.services;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.exterminator3618.server.models.UserInfo;
import io.exterminator3618.server.models.UserStatistics;
import io.exterminator3618.server.utils.ForbiddenAction;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class MatchFindService {

    public static final int ONLINE_STATUS_CACHE_MINUTES = 1;
    public static final int MATCH_QUEUE_TIMEOUT_SECONDS = 20;
    public static final int MATCH_INVITE_TIMEOUT_SECONDS = 60;

    private final RoomService roomService;

    /**
     * Cache to track if a user is currently online.
     * Value is true if the user is online, false if invisible mode, and null if offline.
     * Entries expire after 2 minutes of inactivity.
     */
    private final Cache<@NonNull Long, Boolean> userOnline = Caffeine.newBuilder()
            .expireAfterWrite(ONLINE_STATUS_CACHE_MINUTES, TimeUnit.MINUTES)
            .build();

    private final Cache<@NonNull Long, Set<Long>> matchInvites = Caffeine.newBuilder()
            .expireAfterWrite(MATCH_INVITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build();

    private final LinkedHashMap<Long, MatchRequest> matchQueue = new LinkedHashMap<>();

    public MatchFindService(RoomService roomService) {
        this.roomService = roomService;

        // Background thread to process the match queue.
        Thread queueWorkerThread = new Thread(() -> {
            while (true) {
                synchronized (matchQueue) {
                    var entry = matchQueue.pollFirstEntry();
                    if (entry == null) {
                        try {
                            matchQueue.wait();
                        } catch (InterruptedException e) {
                            log.error("Match queue worker interrupted", e);
                            break;
                        }
                        continue;
                    }
                    var req = entry.getValue();
                    if (req.getLastInteraction().plusSeconds(MATCH_QUEUE_TIMEOUT_SECONDS).isBefore(LocalDateTime.now())) {
                        log.debug("Removing inactive user ID {} from match queue", req.accountId);
                        if (req.getMatchedAccountId() != null) {
                            clearMatch(req.getMatchedAccountId());
                        }
                        continue;
                    }
                    // TODO: implement accept match logic, as i didn't have enough time to finish it
                    // else if (req.isAcceptedMatch()) {
                    //     MatchRequest other = req.getMatchedAccountId() == null ? null : matchQueue.remove(req.getMatchedAccountId());
                    //     if (other != null) {
                    //         if (other.isAcceptedMatch()) {
                    //             log.debug("User ID {} accepted match with user ID {}", req.accountId, req.matchedAccountId);
                    //             createMatchRoom(req, other);
                    //             continue;
                    //         } else {
                    //             matchQueue.put(other.getAccountId(), other);
                    //         }
                    //     } else {
                    //         log.warn("User ID {} accepted match but no matched account ID found", req.accountId);
                    //         clearMatch(req.getAccountId());
                    //     }
                    // }
                    else if (req.getMatchedAccountId() == null) {
                        // TODO: make a better algorithm to find the best match
                        AtomicReference<MatchRequest> smallestDiffReq = new AtomicReference<>();
                        matchQueue.forEach((accountId, other) -> {
                            if (other.getAccountId() == req.getAccountId()) return;
                            if (smallestDiffReq.get() == null) smallestDiffReq.set(other);
                            else {
                                int diff1 = Math.abs(other.getAverageScore() - req.getAverageScore());
                                int diff2 = Math.abs(smallestDiffReq.get().getAverageScore() - req.getAverageScore());
                                if (diff1 < diff2) {
                                    smallestDiffReq.set(other);
                                }
                            }
                        });
                        if (smallestDiffReq.get() != null) {
                            MatchRequest other = smallestDiffReq.get();
                            req.setMatchedAccountId(other.getAccountId());
                            other.setMatchedAccountId(req.getAccountId());
                            log.debug("Matched user ID {} with user ID {}", req.accountId, other.getAccountId());
                            // create match room immediately for now
                            roomService.createRoom(req.getAccountId(), other.getAccountId());
                        }
                    }
                    matchQueue.put(req.accountId, req);
                }
            }
        });
        queueWorkerThread.setName("MatchService-QueueWorker");
        queueWorkerThread.setDaemon(true);
        queueWorkerThread.start();
    }

    /**
     * Checks if a user is currently online.
     *
     * @param accountId The account ID of the user.
     * @return true if the user is online, false otherwise.
     */
    public boolean isOnline(long accountId) {
        return Boolean.TRUE.equals(userOnline.getIfPresent(accountId));
    }

    /**
     * Sets a user as online.
     *
     * @param accountId The account ID of the user.
     */
    public void setOnline(long accountId) {
        userOnline.put(accountId, true);
        // Update last interaction time if in match queue
        synchronized (matchQueue) {
            var currentMatchReq = matchQueue.get(accountId);
            if (currentMatchReq != null) {
                currentMatchReq.setLastInteraction(LocalDateTime.now());
            }
        }
    }

    /**
     * Adds a user to the match queue.
     *
     * @param accountId account ID of the user
     * @param userStatistics user statistics
     */
    public void joinMatchQueue(long accountId, UserStatistics userStatistics) {
        synchronized (matchQueue) {
            if (matchQueue.containsKey(accountId)) {
                throw new ForbiddenAction("Already in match queue");
            }
            log.debug("Account ID {} joined the match queue", accountId);
            matchQueue.put(accountId, new MatchRequest(accountId, userStatistics));
            matchQueue.notify();
        }
    }

    /**
     * Removes a user from the match queue.
     *
     * @param accountId account ID of the user
     */
    public void leaveMatchQueue(long accountId) {
        synchronized (matchQueue) {
            var req = matchQueue.get(accountId);
            if (req == null) {
                throw new ForbiddenAction("Not in match queue");
            }
            // if (req.acceptedMatch) {
            //     throw new Forbidden("Cannot leave match queue after accepting a match");
            // }
            if (req.matchedAccountId != null) {
                clearMatch(req.matchedAccountId);
            }
            log.debug("Account ID {} left the match queue", accountId);
            matchQueue.remove(accountId);
        }
    }

    // public boolean isMatchFound(long accountId) {
    //     synchronized (matchQueue) {
    //         var req = matchQueue.get(accountId);
    //         if (req == null) {
    //             throw new Forbidden("Not in match queue");
    //         }
    //         req.setLastInteraction(LocalDateTime.now());
    //         return req.getMatchedAccountId() != null;
    //     }
    // }

    // public boolean acceptMatch(long accountId) {
    //     synchronized (matchQueue) {
    //         var req = matchQueue.get(accountId);
    //         if (req == null) {
    //             throw new Forbidden("Not in match queue");
    //         }
    //         if (req.getMatchedAccountId() == null) {
    //             throw new Forbidden("No match found to accept");
    //         }
    //         req.setLastInteraction(LocalDateTime.now());
    //         if (!req.isAcceptedMatch()) {
    //             req.setAcceptedMatch(true);
    //             log.debug("Account ID {} accepted match with account ID {}", accountId, req.getMatchedAccountId());
    //         }
    //         var other = matchQueue.get(req.getMatchedAccountId());
    //         return other != null && other.isAcceptedMatch();
    //     }
    // }

    public void inviteFriend(long accountId, long friendAccountId) {
        if (roomService.isInRoom(accountId) || roomService.isInRoom(friendAccountId)) {
            throw new ForbiddenAction("One of the users is already in a room");
        }
        var inviteList = matchInvites.getIfPresent(friendAccountId);
        if (inviteList == null) {
            inviteList = Set.of(accountId);
        } else {
            inviteList.add(accountId);
        }
        matchInvites.put(friendAccountId, inviteList);
    }

    @NonNull
    public Set<Long> getPendingInvites(long accountId) {
        var inviteList = matchInvites.getIfPresent(accountId);
        if (inviteList == null) {
            return Set.of();
        }
        return inviteList;
    }

    public void acceptInvite(long accountId, long friendAccountId) {
        var inviteList = matchInvites.getIfPresent(accountId);
        if (inviteList == null || !inviteList.contains(friendAccountId)) {
            throw new ForbiddenAction("No invite from this user");
        }
        if (roomService.isInRoom(accountId) || roomService.isInRoom(friendAccountId)) {
            throw new ForbiddenAction("One of the users is already in a room");
        }
        matchInvites.invalidate(accountId);
        synchronized (matchQueue) {
            matchQueue.remove(accountId);
            matchQueue.remove(friendAccountId);
        }
        roomService.createRoom(accountId, friendAccountId);
    }

    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = false)
    private static class MatchRequest extends UserStatistics {

        private final long accountId;

        public MatchRequest(long accountId, UserStatistics userStatistics) {
            super(userStatistics);
            this.accountId = accountId;
        }

        private LocalDateTime lastInteraction = LocalDateTime.now();

        private Long matchedAccountId = null;
        // private boolean acceptedMatch = false;

    }

    private void clearMatch(long accountId) {
        var req = matchQueue.get(accountId);
        if (req != null) {
            req.setMatchedAccountId(null);
            // req.setAcceptedMatch(false);
            log.debug("Clear match of user ID {}", req.accountId);
        }
    }

}

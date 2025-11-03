package io.exterminator3618.server.services;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import io.exterminator3618.server.data.Account;
import io.exterminator3618.server.data.Match;
import io.exterminator3618.server.models.RoomStatus;
import io.exterminator3618.server.repositories.AccountRepository;
import io.exterminator3618.server.repositories.MatchRepository;
import io.exterminator3618.server.repositories.RecordRepository;
import io.exterminator3618.server.utils.ForbiddenAction;
import io.exterminator3618.server.utils.LevelPool;
import io.exterminator3618.server.utils.Room;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final AccountRepository accountRepository;
    private final MatchRepository matchRepository;
    private final RecordRepository recordRepository;

    private final Cache<@NonNull Long, Room> rooms = Caffeine.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .removalListener((Long roomId, Room room, @NonNull RemovalCause cause) -> {
                saveRoomResults(room);
            })
            .build();

    private final Cache<@NonNull Long, Long> accountIdToRoomIdMap = Caffeine.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .removalListener((Long accountId, Long roomId, @NonNull RemovalCause cause) -> {
                log.debug("Account {} removed from room {} due to {}", accountId, roomId, cause);
                var roomInstance = rooms.getIfPresent(roomId);
                if (roomInstance != null) {
                    roomInstance.setFinished(accountId);
                }
            })
            .build();

    private final Cache<@NonNull Long, Account> accountCache = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();


    public boolean isInRoom(long accountId) {
        return getRoomInstance(accountId) != null;
    }

    public void createRoom(long... accountIds) {
        var mapCode = LevelPool.getRandomMapCode();
        log.debug("Creating room with default mode, map code {} for accounts: {}", mapCode, accountIds);
        var roomInstance = new Room(mapCode, accountIds);
        var roomId = System.currentTimeMillis();
        rooms.put(roomId, roomInstance);
        for (long accountId : accountIds) {
            accountIdToRoomIdMap.put(accountId, roomId);
        }
    }

    public void leaveRoom(long accountId) {
        var roomInstance = getRoomInstance(accountId);
        if (roomInstance == null) {
            throw new ForbiddenAction("Account is not in a room");
        }
        accountIdToRoomIdMap.invalidate(accountId);
    }

    public RoomStatus getRoomStatus(long accountId) {
        var roomInstance = getRoomInstance(accountId);
        if (roomInstance == null) {
            throw new ForbiddenAction("Account is not in a room");
        }
        var memberStatus = new ArrayList<RoomStatus.MemberStatus>();
        AtomicInteger finishedCount = new AtomicInteger();
        roomInstance.getMembers().forEach((memberId, recordUtility) -> {
            var account = getAccount(memberId);
            if (account != null) {
                var playerStatus = new RoomStatus.MemberStatus();
                playerStatus.setPlayerName(account.getName());
                playerStatus.setPlayerUsername(account.getUsername());
                playerStatus.setScore(recordUtility.getCurrentScore());
                playerStatus.setCombo(recordUtility.getCurrentCombo());
                playerStatus.setMaxCombo(recordUtility.getMaxCombo());
                playerStatus.setHp(recordUtility.getCurrentHp());
                playerStatus.setFinished(recordUtility.isFinished());
                if (recordUtility.isFinished()) {
                    finishedCount.getAndIncrement();
                }
                memberStatus.add(playerStatus);
            }
        });
        var roomStatus = new RoomStatus(memberStatus.toArray(new RoomStatus.MemberStatus[] {}));
        roomStatus.setMode(roomInstance.getMode());
        roomStatus.setMapCode(roomInstance.getMapCode());
        roomStatus.setTotalPlayers(roomInstance.getMembers().size());
        roomStatus.setFinishedPlayers(finishedCount.intValue());
        return roomStatus;
    }

    public void pushGameEvents(long accountId, String rawGameEventData) {
        var roomInstance = getRoomInstance(accountId);
        if (roomInstance == null) {
            throw new ForbiddenAction("Account is not in a room");
        }
        roomInstance.pushGameEvents(accountId, rawGameEventData);
    }

    private Room getRoomInstance(long accountId) {
        var roomId = accountIdToRoomIdMap.getIfPresent(accountId);
        if (roomId == null) {
            return null;
        }
        return rooms.getIfPresent(roomId);
    }

    private Account getAccount(long accountId) {
        var account = accountCache.getIfPresent(accountId);
        if (account == null) {
            account = accountRepository.findAccountById(accountId);
            if (account != null) {
                accountCache.put(accountId, account);
            }
        }
        return account;
    }

    private void saveRoomResults(Room room) {
        var match = new Match();
        match.setMode(room.getMode());
        match.setMapCode(room.getMapCode());
        matchRepository.save(match);
        room.getMembers().forEach((memberId, recordUtility) -> {
            var account = getAccount(memberId);
            if (account != null) {
                var record = recordUtility.export(account, match);
                recordRepository.save(record);
            }
        });
    }

}

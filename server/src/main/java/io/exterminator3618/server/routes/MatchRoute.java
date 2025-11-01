package io.exterminator3618.server.routes;

import io.exterminator3618.server.models.OperationResponse;
import io.exterminator3618.server.models.RoomStatus;
import io.exterminator3618.server.models.UserStatus;
import io.exterminator3618.server.repositories.AccountRepository;
import io.exterminator3618.server.repositories.FriendshipRepository;
import io.exterminator3618.server.repositories.RecordRepository;
import io.exterminator3618.server.services.MatchFindService;
import io.exterminator3618.server.services.RoomService;
import io.exterminator3618.server.utils.ForbiddenAction;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchRoute {

    private final AccountRepository accountRepository;
    private final FriendshipRepository friendshipRepository;
    private final RecordRepository recordRepository;
    private final MatchFindService matchFindService;
    private final RoomService roomService;

    @GetMapping("/status")
    public UserStatus getStatus(@RequestAttribute(name = "userId") Long userId) {
        matchFindService.setOnline(userId);
        var userInMatch = roomService.isInRoom(userId);
        var friendInvites = matchFindService.getPendingInvites(userId);
        ArrayList<UserStatus.MatchInvite> inviteInfo = new ArrayList<>();
        for (var friendInvite : friendInvites) {
            var friendAccount = accountRepository.findAccountById(friendInvite);
            if (friendAccount == null) {
                continue;
            }
            inviteInfo.add(new UserStatus.MatchInvite(
                    friendAccount.getName(),
                    friendAccount.getUsername()
            ));
        }
        return new UserStatus(userInMatch, inviteInfo.toArray(new UserStatus.MatchInvite[]{}));
    }

    @GetMapping("/roomStatus")
    public RoomStatus getRoomStatus(@RequestAttribute(name = "userId") Long userId) {
        matchFindService.setOnline(userId);
        try {
            return roomService.getRoomStatus(userId);
        } catch (ForbiddenAction e) {
            return null; // User is not in a room
        }
    }

    @PostMapping("/joinDailyRankingMatch")
    public OperationResponse joinDailyRankingMatch(@RequestAttribute(name = "userId") Long userId) {
        return new OperationResponse(false, "Daily ranking matches are not available at the moment");
    }

    @PostMapping("/joinQueue")
    public OperationResponse joinMatchQueue(@RequestAttribute(name = "userId") Long userId) {
        var userStatistics = recordRepository.getUserStatisticsByAccountId(userId);
        if (userStatistics == null) {
            return new OperationResponse(false, "Cannot get user statistics to join match queue");
        }
        try {
            matchFindService.joinMatchQueue(userId, userStatistics);
            return new OperationResponse(true, "Joined match queue successfully");
        } catch (ForbiddenAction e) {
            return new OperationResponse(false, "You are already in the match queue");
        }
    }

    @PostMapping("/leaveQueue")
    public OperationResponse leaveMatchQueue(@RequestAttribute(name = "userId") Long userId) {
        try {
            matchFindService.leaveMatchQueue(userId);
            return new OperationResponse(true, "Left match queue successfully");
        } catch (ForbiddenAction e) {
            return new OperationResponse(false, "You are not in the match queue");
        }
    }

    @PostMapping("/inviteFriend")
    public OperationResponse inviteFriend(@RequestAttribute(name = "userId") Long userId, @RequestBody String friendUsername) {
        var friendship = friendshipRepository.findFriendshipByAccountIdAndFriendUsername(userId, friendUsername);
        if (friendship == null) {
            return new OperationResponse(false, "User " + friendUsername + " is not your friend");
        }
        var otherAccount = friendship.getAccount1();
        if (otherAccount.getId().equals(userId)) {
            otherAccount = friendship.getAccount2();
        }
        try {
            matchFindService.inviteFriend(userId, otherAccount.getId());
            return new OperationResponse(true, "Match invite sent to " + friendUsername);
        } catch (ForbiddenAction e) {
            return new OperationResponse(false, "Cannot send match invite to " + friendUsername + ": " + e.getMessage());
        }
    }

    @PostMapping("/acceptMatchInvite")
    public OperationResponse acceptMatchInvite(@RequestAttribute(name = "userId") Long userId, @RequestBody Long friendUsername) {
        try {
            matchFindService.acceptInvite(userId, friendUsername);
            return new OperationResponse(true, "Match invite accepted from " + friendUsername);
        } catch (ForbiddenAction e) {
            return new OperationResponse(false, "Cannot accept match invite from " + friendUsername + ": " + e.getMessage());
        }
    }

    @PostMapping("/pushGameEvents")
    public OperationResponse pushGameEvents(@RequestAttribute(name = "userId") Long userId, @RequestBody List<String> rawGameEventsData) {
        try {
            rawGameEventsData.forEach((data) -> {
                if (data == null || data.isEmpty()) {
                    throw new IllegalArgumentException("No game event data provided");
                }
                roomService.pushGameEvents(userId, data);
            });
            return new OperationResponse(true, "All game events pushed successfully");
        } catch (Exception e) {
            return new OperationResponse(false, "Cannot push game events: " + e.getMessage());
        }
    }

    @PostMapping("/leaveRoom")
    public OperationResponse leaveRoom(@RequestAttribute(name = "userId") Long userId) {
        try {
            roomService.leaveRoom(userId);
            return new OperationResponse(true, "Left room successfully");
        } catch (ForbiddenAction e) {
            return new OperationResponse(false, "Cannot leave room: " + e.getMessage());
        }
    }

}

package io.exterminator3618.server.routes;

import io.exterminator3618.server.data.Account;
import io.exterminator3618.server.data.Friendship;
import io.exterminator3618.server.models.FriendUsernameRequest;
import io.exterminator3618.server.models.OperationResponse;
import io.exterminator3618.server.models.UserInfo;
import io.exterminator3618.server.repositories.AccountRepository;
import io.exterminator3618.server.repositories.FriendshipRepository;
import io.exterminator3618.server.repositories.RecordRepository;
import io.exterminator3618.server.services.MatchFindService;
import io.exterminator3618.server.services.RoomService;
import io.exterminator3618.server.utils.InvalidRequestException;
import io.exterminator3618.server.utils.MissingRequestFieldException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@Slf4j
@RequiredArgsConstructor
public class FriendRoute {

    private final AccountRepository accountRepository;
    private final FriendshipRepository friendshipRepository;
    private final RecordRepository recordRepository;
    private final MatchFindService matchFindService;
    private final RoomService roomService;

    @GetMapping("/list")
    public List<UserInfo> getFriendList(@RequestAttribute(name = "userId") Long userId) {
        var friendships = friendshipRepository.findFriendshipsByAccountId(userId);
        return friendships.stream().map(friendship -> {
            Account other;
            if (friendship.getAccount1().getId().equals(userId)) {
                other = friendship.getAccount2();
            } else {
                other = friendship.getAccount1();
            }
            var friendStats = recordRepository.getUserStatisticsByAccountId(other.getId());
            var model = new UserInfo(other, friendStats);
            if (!other.isInvisibleMode()) {
                model.setOnline(matchFindService.isOnline(other.getId()));
                model.setInMatch(roomService.isInRoom(other.getId()));
            }
            return model;
        }).toList();
    }

    @PostMapping("/add")
    public OperationResponse addFriend(@RequestAttribute(name = "userId") Long userId, @RequestBody FriendUsernameRequest req) {
        String friendUsername = req.friendAccountUsername();
        if (friendUsername == null) {
            throw new MissingRequestFieldException("Username cannot be null");
        }
        var existFriendship = friendshipRepository.findFriendshipByAccountIdAndFriendUsername(userId, friendUsername);
        if (existFriendship != null) {
            return new OperationResponse(false, "User " + friendUsername + " is already your friend");
        }
        var user = accountRepository.findAccountById(userId);
        var other = accountRepository.findAccountByUsername(friendUsername);
        if (user == null) {
            throw new InvalidRequestException("Account not found");
        }
        if (other == null) {
            return new OperationResponse(false, "User not found");
        }
        if (user.getId().longValue() == other.getId().longValue()) {
            return new OperationResponse(false, "Cannot add friend with yourself");
        }
        var friendship = new Friendship();
        friendship.setAccount1(user);
        friendship.setAccount2(other);
        friendshipRepository.save(friendship);
        return new OperationResponse(true, "Successfully added friend with " + friendUsername);
    }

    @PostMapping("/remove")
    public OperationResponse removeFriend(@RequestAttribute(name = "userId") Long userId, @RequestBody FriendUsernameRequest req) {
        String friendUsername = req.friendAccountUsername();
        if (friendUsername == null) {
            throw new MissingRequestFieldException("Username cannot be null");
        }
        var friendship = friendshipRepository.findFriendshipByAccountIdAndFriendUsername(userId, friendUsername);
        if (friendship == null) {
            return new OperationResponse(false, "User is not your friend");
        }
        friendshipRepository.delete(friendship);
        return new OperationResponse(true, "Friend removed successfully");
    }

}

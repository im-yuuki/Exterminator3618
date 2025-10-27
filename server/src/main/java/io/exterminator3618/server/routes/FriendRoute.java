package io.exterminator3618.server.routes;

import io.exterminator3618.server.data.Account;
import io.exterminator3618.server.data.Friendship;
import io.exterminator3618.server.models.FriendAuditRequest;
import io.exterminator3618.server.models.FriendStatusModel;
import io.exterminator3618.server.models.OperationResponse;
import io.exterminator3618.server.repositories.FriendsRepository;
import io.exterminator3618.server.services.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@Slf4j
@RequiredArgsConstructor
public class FriendRoute {

    private final FriendsRepository friendsRepository;
    private final MatchService matchService;

    @GetMapping("/list")
    public List<FriendStatusModel> getFriendList(@RequestParam(name = "userId") Long userId) {
        var friendships = friendsRepository.findFriendshipsByAccountId(userId);
        return friendships.stream().map(friendship -> {
            Account other;
            if (friendship.getAccount1().getId().equals(userId)) {
                other = friendship.getAccount2();
            } else {
                other = friendship.getAccount1();
            }
            var model = new FriendStatusModel(other.getName(), other.getUsername());
            if (other.isInvisibleMode()) {
                model.setInMatchOrOnlineStatus(null);
            } else {
                model.setInMatchOrOnlineStatus(matchService.getUserStatus(other.getId()));
            }
            return model;
        }).toList();
    }

    @PostMapping("/add")
    public OperationResponse addFriend(@RequestParam(name = "userId") Long userId, @RequestBody FriendAuditRequest req) {
        var friend = friendsRepository.findFriendshipByAccountIdAndFriendUsername(userId, req.friendAccountUsername());
        if (friend != null) {
            return new OperationResponse(false, "User is already your friend");
        }
        var newFriendship = new Friendship();
        // TODO: cook it :sob:
        return new OperationResponse(false, "Feature not implemented");
    }

    @PostMapping("/remove")
    public OperationResponse removeFriend(@RequestParam(name = "userId") Long userId, @RequestBody FriendAuditRequest req) {
        var friend = friendsRepository.findFriendshipByAccountIdAndFriendUsername(userId, req.friendAccountUsername());
        if (friend == null) {
            return new OperationResponse(false, "User is not your friend");
        }
        friendsRepository.delete(friend);
        return new OperationResponse(true, "Friend removed successfully");
    }

}

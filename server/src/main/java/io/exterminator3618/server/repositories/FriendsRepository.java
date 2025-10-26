package io.exterminator3618.server.repositories;

import io.exterminator3618.server.data.Friendship;
import io.exterminator3618.server.utils.FriendshipId;
import org.springframework.data.repository.CrudRepository;

public interface FriendsRepository extends CrudRepository<Friendship, FriendshipId> {

}

package io.exterminator3618.server.repositories;

import io.exterminator3618.server.data.Friendship;
import io.exterminator3618.server.utils.FriendshipId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendsRepository extends CrudRepository<Friendship, FriendshipId> {

    @Query("""
            SELECT f
            FROM Friendship f
            WHERE f.account1.id = :accountId or f.account2.id = :accountId
            """)
    List<Friendship> findFriendshipsByAccountId(
            @Param("accountId") Long accountId
    );

    @Query("""
           SELECT f
           FROM Friendship f
           WHERE (f.account1.id = :accountId AND f.account2.username = :friendUsername)
              OR (f.account2.id = :accountId AND f.account1.username = :friendUsername)
           """)
    Friendship findFriendshipByAccountIdAndFriendUsername(
            @Param("accountId") Long accountId,
            @Param("friendUsername") String friendUsername
    );

}

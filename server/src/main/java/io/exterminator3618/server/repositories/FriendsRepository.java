package io.exterminator3618.server.repositories;

import io.exterminator3618.server.data.Friendship;
import io.exterminator3618.server.utils.FriendshipId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendsRepository extends CrudRepository<Friendship, FriendshipId> {

    @Query("""
            SELECT DISTINCT
              CASE WHEN account1.id = :accountId THEN account2 ELSE account1 END
            FROM Friendship
            WHERE account1.id = :accountId or account2.id = :accountId
            """)
    List<Friendship> findFriendshipsByAccountId(@Param("accountId") Long accountId);

    // @Query("""
    //        SELECT
    //           CASE
    //             WHEN account1.id = :accountId1 THEN account2
    //             WHEN account2.id = :accountId1 THEN account1
    //           END
    //           FROM Friendship
    //           WHERE (account1.id = :accountId1 AND account2.id = :accountId2)
    //                 OR (account1.id = :accountId2 AND account2.id = :accountId1)
    //        """)
    // Friendship findFriendshipByAccountIds(@Param("accountId1") Long accountId1, @Param("accountId2") Long accountId2);

    @Query("""
           SELECT Friendship
           FROM Friendship
           WHERE (account1.id = :accountId AND account2.username = :friendUsername)
                 OR (account2.id = :accountId AND account1.username = :friendUsername)
           """)
    Friendship findFriendshipByAccountIdAndFriendUsername(Long accountId, String friendUsername);

}

package io.exterminator3618.server.repositories;

import io.exterminator3618.server.data.Record;
import io.exterminator3618.server.models.UserStatistics;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends CrudRepository<Record, Long> {

    @Query("""
            SELECT new io.exterminator3618.server.models.UserStatistics(
                AVG(r.score), MAX(r.score), COUNT(r)
            ) FROM Record r
            WHERE r.account.id = :accountId
            """)
    UserStatistics getUserStatisticsByAccountId(
            @Param("accountId") long accountId
    );

    @Query("""
            SELECT new io.exterminator3618.server.models.UserStatistics(
                AVG(r.score), MAX(r.score), COUNT(r)
            ) FROM Record r
            WHERE r.account.username = :username
            """)
    UserStatistics getUserStatisticsByUsername(
            @Param("username") String username
    );

}

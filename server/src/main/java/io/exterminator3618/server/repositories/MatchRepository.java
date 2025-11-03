package io.exterminator3618.server.repositories;

import io.exterminator3618.server.data.Match;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends CrudRepository<Match, Long> {

}

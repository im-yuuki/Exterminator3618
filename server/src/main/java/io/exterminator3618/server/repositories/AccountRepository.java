package io.exterminator3618.server.repositories;

import io.exterminator3618.server.data.Account;
import org.springframework.data.domain.Limit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    Account findAccountByUsername(String username);

}

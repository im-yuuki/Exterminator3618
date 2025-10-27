package io.exterminator3618.server.repositories;

import io.exterminator3618.server.data.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    Account findAccountByUsername(String username);

}

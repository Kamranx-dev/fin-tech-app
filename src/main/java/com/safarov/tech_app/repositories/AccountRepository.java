package com.safarov.tech_app.repositories;

import com.safarov.tech_app.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("select a.accountNo from Account a where a.accountNo in :accountNos")
    List<Integer> getExistsAccountNo(@Param("accountNos") List<Integer> accountNos);

    Optional<Account> findByAccountNo(Integer accountNo);
}


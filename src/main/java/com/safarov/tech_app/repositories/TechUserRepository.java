package com.safarov.tech_app.repositories;

import com.safarov.tech_app.entity.TechUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface TechUserRepository extends JpaRepository<TechUser, Long> {

    boolean existsByPin(String pin);

    @Query("select t from TechUser t join fetch t.accountList where t.pin =:pin")
    Optional<TechUser> findByPin(@Param("pin") String pin);
}

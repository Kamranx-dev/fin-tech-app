package com.safarov.techapp.repositories;

import com.safarov.techapp.entity.TechUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<TechUser, Long> {

    @Query("select u from TechUser u join fetch u.accountList where u.pin=:pin")
    Optional<TechUser> findUserByPin(@Param("pin") String pin);
}

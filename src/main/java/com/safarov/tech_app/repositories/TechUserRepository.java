package com.safarov.tech_app.repositories;

import com.safarov.tech_app.entity.TechUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TechUserRepository extends JpaRepository<TechUser, Long> {

    boolean existsByPin(String pin);

}

package com.thales.oauth.repository;

import com.thales.oauth.domain.UserSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSystemRepository extends JpaRepository<UserSystem, Integer> {

    UserSystem findByLoginIgnoreCase(String login);

    UserSystem findByLogin(String login);

    UserSystem findByCustomerId(String customerId);

}

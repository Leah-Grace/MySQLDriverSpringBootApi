package com.LeahGrace.MySQLDriverSpringBootApi.repositories;

import com.LeahGrace.MySQLDriverSpringBootApi.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); //Optional<User> since the request to findByUsername may not find a user with that username

    Boolean existsByUsername(String username);


}

package com.LeahGrace.MySQLDriverSpringBootApi.repositories;

import com.LeahGrace.MySQLDriverSpringBootApi.models.Developer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {
}

package com.jacketus.RSOI_Lab4.authservice.repositories;

import com.jacketus.RSOI_Lab4.authservice.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

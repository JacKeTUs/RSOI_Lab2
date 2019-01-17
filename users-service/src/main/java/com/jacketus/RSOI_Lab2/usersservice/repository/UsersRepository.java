package com.jacketus.RSOI_Lab2.usersservice.repository;

import com.jacketus.RSOI_Lab2.usersservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
}

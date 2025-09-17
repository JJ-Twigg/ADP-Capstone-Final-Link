package com.college.repository;

import com.college.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(String username);

    User findByEmail(String email);
}
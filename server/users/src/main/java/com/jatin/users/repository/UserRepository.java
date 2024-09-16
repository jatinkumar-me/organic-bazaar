package com.jatin.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jatin.users.model.User;

/**
 * UserRepository
 */
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User findByEmail(String username);

}

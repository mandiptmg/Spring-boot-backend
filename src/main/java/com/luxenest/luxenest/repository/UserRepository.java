package com.luxenest.luxenest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luxenest.luxenest.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // Method to check if the email already exists in the database
    boolean existsByEmail(String email);
}

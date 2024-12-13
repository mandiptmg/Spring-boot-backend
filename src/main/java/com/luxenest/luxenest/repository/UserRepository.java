package com.luxenest.luxenest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luxenest.luxenest.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Method to check if the email already exists in the database
    Boolean existsByEmail(String email);

    User findByEmail(String email);

}

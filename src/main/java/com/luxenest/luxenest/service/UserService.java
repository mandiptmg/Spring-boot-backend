package com.luxenest.luxenest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.luxenest.luxenest.model.User;
import com.luxenest.luxenest.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) { 
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id).orElseThrow(() -> new RuntimeException("User not Found"));

        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        user.setRole(userDetails.getRole());

        return userRepository.save(user);
    }

    // void means no return
    public boolean deleteUser(Long id) {
        userRepository.deleteById(id);
        return true;
    }
}

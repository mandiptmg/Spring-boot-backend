package com.luxenest.luxenest.controller;

import com.luxenest.luxenest.model.ApiResponse;
// import com.luxenest.luxenest.exception.CustomException;
import com.luxenest.luxenest.model.User;
import com.luxenest.luxenest.repository.UserRepository;
import com.luxenest.luxenest.service.UserService;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class userController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();

        if (allUsers != null && !allUsers.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("Users", allUsers);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/add-user")
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody User user) {

        // Check if the email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            ApiResponse<User> errorResponse = new ApiResponse<>(
                    "error",
                    HttpStatus.BAD_REQUEST.value(),
                    "Email is already in use",
                    null,
                    java.time.LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            // Create the user if email is unique
            User createUser = userService.createUser(user);
            ApiResponse<User> response = new ApiResponse<>(
                    "success",
                    HttpStatus.CREATED.value(),
                    "User created Successfully",
                    createUser,
                    java.time.LocalDateTime.now().toString());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // Log the exception for debugging and throw a specific error
            ApiResponse<User> response = new ApiResponse<>(
                    "success",
                    HttpStatus.CREATED.value(),
                    "Failed to create user",
                    null,
                    java.time.LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ResponseEntity is the class in the Spring Framework that represents the
    // entire HTTP response, including the status code, headers, and body.

    // build is a method that constructs or finalizes the ResponseEntity object.

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (!user.isPresent()) {
            ApiResponse<User> errorResponse = new ApiResponse<>(
                    "error",
                    HttpStatus.NOT_FOUND.value(),
                    "User not found",
                    null,
                    java.time.LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        ApiResponse<User> successResponse = new ApiResponse<>(
                "success",
                HttpStatus.OK.value(),
                "User found",
                user.get(),
                java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(successResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUserById(@PathVariable Long id,
            @Valid @RequestBody User userDetails) {
        User user = userService.updateUser(id, userDetails);
        if (user == null) {
            ApiResponse<User> errorResponse = new ApiResponse<>(
                    "error",
                    HttpStatus.NOT_FOUND.value(),
                    "User not found",
                    null,
                    java.time.LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        ApiResponse<User> response = new ApiResponse<>(
                "success",
                HttpStatus.OK.value(),
                "User found",
                user,
                java.time.LocalDateTime.now().toString());
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

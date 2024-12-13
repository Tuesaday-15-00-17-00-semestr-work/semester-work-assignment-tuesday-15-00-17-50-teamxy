package com.librarymanagement.LibraryManagmentServer.controler;

import com.librarymanagement.LibraryManagmentServer.model.User;
import com.librarymanagement.LibraryManagmentServer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // User registration
    @PostMapping("/register")
    public ResponseEntity<User> registerUser (@RequestBody User user) {
        User registeredUser  = userService.registerUser (user);
        return new ResponseEntity<>(registeredUser , HttpStatus.CREATED);
    }

    // User login
    @PostMapping("/login")
    public ResponseEntity<String> loginUser (@RequestBody User user) {
        String token = userService.loginUser (user.getUsername(), user.getPassword());
        if (token != null) {
            return new ResponseEntity<>(token, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }

    // Create a new user (admin functionality)
    @PostMapping
    public ResponseEntity<User> createUser (@RequestBody User user) {
        User createdUser  = userService.createUser (user);
        return new ResponseEntity<>(createdUser , HttpStatus.CREATED);
    }

    // Get a user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser ById(@PathVariable Long userId) {
        User user = userService.getUser ById(userId);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Update a user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser (@PathVariable Long id, @RequestBody User user) {
        User updatedUser  = userService.updateUser (id, user);
        if (updatedUser  != null) {
            return new ResponseEntity<>(updatedUser , HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser (@PathVariable Long id) {
        boolean isDeleted = userService.deleteUser (id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
package com.librarymanagement.LibraryManagmentServer.service;

import com.librarymanagement.LibraryManagmentServer.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private List<User> users = new ArrayList<>();
    private Long userIdCounter = 1L;

    // Register a new user
    public User registerUser (User user) {
        user.setUser Id(userIdCounter++);
        users.add(user);
        return user;
    }

    // Login user
    public String loginUser (String username, String password) {
        for (User  user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                // Generate a simple token (for demonstration purposes)
                return "token-" + user.getUser Id(); // In a real application, use JWT or similar
            }
        }
        return null; // Invalid credentials
    }

    // Create a new user (admin functionality)
    public User createUser (User user) {
        user.setUser Id(userIdCounter++);
        users.add(user);
        return user;
    }

    // Get a user by ID
    public User getUser ById(Long id) {
        return users.stream()
                .filter(user -> user.getUser Id().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Get all users
    public List<User> getAllUsers() {
        return users;
    }

    // Update a user
    public User updateUser (Long id, User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUser Id().equals(id)) {
                user.setUser Id(id); // Ensure the ID remains the same
                users.set(i, user);
                return user;
            }
        }
        return null; // User not found
    }

    // Delete a user
    public boolean deleteUser (Long id) {
        return users.removeIf(user -> user.getUser Id().equals(id));
    }
}

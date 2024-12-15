package com.librarymanagement.service;

import com.librarymanagement.model.User;
import com.librarymanagement.model.Role;
import com.librarymanagement.repository.UserRepository;
import com.librarymanagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void registerUser(String username, String plainPassword) {
        // Encode the password before saving
        String encodedPassword = passwordEncoder.encode(plainPassword);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        userRepository.save(user);

        public User getUserByUsername(String username) {
            return userRepository.findByUsername(username);
        }
    }
}

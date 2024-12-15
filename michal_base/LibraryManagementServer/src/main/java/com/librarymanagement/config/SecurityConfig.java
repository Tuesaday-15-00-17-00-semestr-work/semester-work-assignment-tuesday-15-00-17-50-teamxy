package com.librarymanagement.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            return http
                    .csrf(csrf -> csrf.disable()) // Disable CSRF only if not needed (e.g., for stateless APIs)
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/auth/**").permitAll() // Public endpoints
                            .requestMatchers("/api/admin/**").hasRole("ADMIN") // Restricted to ADMIN users
                            .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN") // USER or ADMIN roles
                            .anyRequest().authenticated() // Secure all other endpoints
                    )
                    .httpBasic(withDefaults()) // HTTP Basic authentication (ensure HTTPS in production)
                    .logout(logout -> logout
                            .logoutUrl("/logout")
                            .invalidateHttpSession(true) // Invalidate session on logout
                            .clearAuthentication(true) // Clear authentication information
                            .permitAll() // Allow everyone to access the logout endpoint
                    )
                    .build();
        } catch (Exception ex) {
            logger.error("Error configuring Security Filter Chain: {}", ex.getMessage(), ex);
            throw new IllegalStateException("Failed to configure Security Filter Chain", ex);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        try {
            // Use BCrypt for secure password encoding
            return new BCryptPasswordEncoder();
        } catch (Exception ex) {
            logger.error("Error initializing Password Encoder: {}", ex.getMessage(), ex);
            throw new IllegalStateException("Failed to initialize Password Encoder", ex);
        }
    }
}
package com.librarymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.librarymanagement")
@EnableJpaRepositories(basePackages = "com.librarymanagement")
public class LibraryServer {

    public static void main(String[] args) {
        SpringApplication.run(LibraryServer.class, args);
    }
}

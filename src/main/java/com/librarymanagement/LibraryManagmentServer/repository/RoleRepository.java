package com.librarymanagement.LibraryManagmentServer.repository;

import com.librarymanagement.LibraryManagmentServer.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}


package com.librarymanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";

    @Id
    private int roleId;

    @Column(nullable = false)
    private String roleName;

    // Getters and Setters
    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}

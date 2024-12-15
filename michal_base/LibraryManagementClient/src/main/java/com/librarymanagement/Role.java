package com.librarymanagement;

public class Role {

    private int roleId;

    public Role() {
    }

    public Role(int roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public Role(boolean b) {
        if (b) {
            roleId = 1;
            roleName="ADMIN";
        }
        else {
            roleId = 2;
            roleName="USER";
        }
    }

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



package com.college.factory;

import com.college.domain.Role;

public class RoleFactory {

    // Create a role
    public static Role createRole(String roleName) {
        return new Role.RoleBuilder()
                .roleName(roleName)
                .build();
    }


}

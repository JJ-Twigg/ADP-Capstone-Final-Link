package com.college.domain;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int id;



    private String roleName;




    private Role(RoleBuilder builder) {
        this.id = builder.id;
        this.roleName = builder.roleName;
    }


    protected Role() {

    }

    // --- Getters ---
    public int getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }



    // --- Builder ---
    public static class RoleBuilder {
        private int id;
        private String roleName;

        public RoleBuilder id(int id) {
            this.id = id;
            return this;
        }

        public RoleBuilder roleName(String roleName) {
            this.roleName = roleName;
            return this;
        }

        public Role build() {
            if (roleName == null || roleName.isBlank()) {
                throw new IllegalArgumentException("Role name cannot be null or empty");
            }
            return new Role(this);
        }
    }
}

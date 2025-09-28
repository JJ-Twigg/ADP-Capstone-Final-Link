package com.college.domain;

import com.college.domain.Role;
import com.college.domain.User;
import jakarta.persistence.*;

//SINCE WE WERE FORCED TO HAVE SPECIFICATION IN BRIDGE TABLE
// JPA WONT AUTO MAKE IT, SO NOW THIS ENTITY IS NEEDED.
// if u dont have an extra attribute in bridge table, jpa makes the bridge for u without an entity like this

@Entity
@Table(name = "user_role")
public class UserRole {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Role role;

    private String specification; // extra column


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }
}

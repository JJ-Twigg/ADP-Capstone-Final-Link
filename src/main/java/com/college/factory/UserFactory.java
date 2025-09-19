package com.college.factory;

import com.college.domain.User;

public class UserFactory {

    //Admin is a superManager who controls managers
    public static User createAdmin(String email, String password) {
        return new User.UserBuilder()
                .email(email)
                .password(password)
                .role("ADMIN")
                .build();
    }

    //Manager is the normal user of the system
    public static User createManager(String email, String password, String role) {
        return new User.UserBuilder()
                .email(email)
                .password(password)
                .role("MANAGER")
                .build();
    }

    //maybe
    public static User createUser(String email, String password, String role) {
        return new User.UserBuilder()
                .email(email)
                .password(password)
                .role(role)
                .build();
    }
}

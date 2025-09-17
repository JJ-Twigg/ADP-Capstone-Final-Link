package com.college.factory;

import com.college.domain.User;

public class UserFactory {

    public static User createAdmin(String email, String password) {
        return new User.UserBuilder()
                .email(email)
                .password(password)
                .role("ADMIN")
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

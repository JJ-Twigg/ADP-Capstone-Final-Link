package com.college.factory;

import com.college.domain.User;
import java.time.LocalDate;

public class UserFactory {

    // Admin is a superManager who controls managers
    public static User createAdmin(String email, String password, String name, String surname, Integer age, String gender) {
        return new User.UserBuilder()
                .email(email)
                .password(password)
                .role("ADMIN")
                .startDate(LocalDate.now())
                .name(name)
                .surname(surname)
                .age(age)
                .gender(gender)
                .build();
    }

    // Manager is a normal user of the system
    public static User createManager(String email, String password, String name, String surname, Integer age, String gender) {
        return new User.UserBuilder()
                .email(email)
                .password(password)
                .role("MANAGER")
                .startDate(LocalDate.now())
                .name(name)
                .surname(surname)
                .age(age)
                .gender(gender)
                .build();
    }

    // Generic user creation with role passed in
    public static User createUser(String email, String password, String name, String surname, Integer age, String gender, String role) {
        return new User.UserBuilder()
                .email(email)
                .password(password)
                .role(role)
                .startDate(LocalDate.now())
                .name(name)
                .surname(surname)
                .age(age)
                .gender(gender)
                .build();
    }
}

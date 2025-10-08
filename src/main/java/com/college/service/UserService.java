package com.college.service;

import com.college.domain.User;
import com.college.factory.UserFactory;
import com.college.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Get all users
    public List<User> getAll() {
        return userRepository.findAll();
    }

    // Create a new user
    public User create(User user) {
        return userRepository.save(user);
    }

    // Read a user by ID
    public User read(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    // Update an existing user
    public User update(User user) {
        if (userRepository.existsById(user.getUserId())) {
            return userRepository.save(user);
        }
        return null; // or throw an exception
    }

    // Delete a user by ID
    public boolean delete(int userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public User register(String email, String pass, String name, String surname, Integer age, String gender, String role) {

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already taken");
        }

        String hashedPassword = passwordEncoder.encode(pass);

        if(role.equals("ADMIN")){
            //
            User user = UserFactory.createAdmin(email, hashedPassword, name, surname, age, gender);
            System.out.println("hashed password: " + hashedPassword);
            System.out.println("saving admin register to db");

            return userRepository.save(user);
        }else if(role.equals("MANAGER")){
            User user = UserFactory.createManager(email, hashedPassword, name, surname, age, gender);
            System.out.println("hashed password: " + hashedPassword);
            System.out.println("saving manager register to db");

            return userRepository.save(user);
        }else{
            User user = UserFactory.createUser(email, hashedPassword, name, surname, age, gender,role);
            System.out.println("hashed password: " + hashedPassword);
            System.out.println("saving user register to db");

            return userRepository.save(user);
        }

    }

    public byte[] getUserPhoto(String email) {
        return userRepository.findByEmail(email)
                .map(User::getImage) // or getPhoto()
                .orElse(null);       // return null if not set
    }


    public void updateUserPhoto(String email, byte[] imageBytes) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setImage(imageBytes); // assuming your entity has "private byte[] image"
            userRepository.save(user);
            System.out.println("Photo updated successfully for user: " + email);
        } else {
            throw new RuntimeException("User not found with email: " + email);
        }
    }


    // Login by email + password
    public User login(String email, String pass) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (passwordEncoder.matches(pass, user.getPassword())) {
                    System.out.println("Login successful!");
                    return user;
                } else {
                    System.out.println("Invalid password");
                }
            } else {
                System.out.println("User not found");
            }
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}

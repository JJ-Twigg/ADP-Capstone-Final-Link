//package com.college.service;
//
//import com.college.domain.User;
//import com.college.factory.UserFactory;
//import com.college.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class UserService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    //autowire a bean, or method. it returns an object password encoder. This can encode passwords
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    // Get all users
//    public List<User> getAll() {
//        return userRepository.findAll();
//    }
//
//    // Create a new user
//    public User create(User user) {
//        return userRepository.save(user);
//    }
//
//    // Read a user by email (primary key)
//    public User read(String email) {
//        return userRepository.findById(email).orElse(null);
//    }
//
//    // Update an existing user
//    public User update(User user) {
//        if (userRepository.existsById(user.getEmail())) {
//            return userRepository.save(user);
//        }
//        return null; // or throw an exception
//    }
//
//    // Delete a user by email
//    public boolean delete(String email) {
//        if (userRepository.existsById(email)) {
//            userRepository.deleteById(email);
//            return true;
//        }
//        return false;
//    }
//
//    public User register(String email, String pass) {
//
//        //check if email is taken, this is a custom method in UserRepo, jpa doesnt have one
//        if(userRepository.existsByEmail(email)){
//            throw new RuntimeException("Email already taken");
//        }
//
//        //use object-bcrypt returned from autowire to encode pass
//        String hashedPassword = passwordEncoder.encode(pass);
//
//        //create User object with factory
//        User user = UserFactory.createAdmin(email, hashedPassword);
//
//        System.out.println("hashed password: " + hashedPassword);
//
//        return userRepository.save(user);
//
//    }
//
//
//
//
//    public User login(String email, String pass) {
//
//        User user;
//
//
//        try {
//            user = userRepository.findByEmail(email); // if returns null
//            if (user != null) {
//                // check password
//                if (passwordEncoder.matches(pass, user.getPassword())) {
//                    System.out.println("Login successful!");
//                    return user;
//                } else {
//                    System.out.println("Invalid password");
//                    return null;
//                }
//            } else {
//                System.out.println("User not found");
//                return null;
//            }
//        } catch (Exception e) {
//            System.out.println("Error during login: " + e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
//
//
//    }
//}
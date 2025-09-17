package com.college.controller;

import com.college.domain.User;
import com.college.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Get all users
    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    // Create a new user
    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    // Read a user by email
    @GetMapping("/{email}")
    public ResponseEntity<User> read(@PathVariable String email) {
        User user = userService.read(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    // Update a user
    @PutMapping("/{email}")
    public ResponseEntity<User> update(@PathVariable String email, @RequestBody User user) {
        user.setEmail(email); // ensure the email matches the path
        User updated = userService.update(user);
        return ResponseEntity.ok(updated);
    }

    // Delete a user
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> delete(@PathVariable String email) {
        boolean deleted = userService.delete(email);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}

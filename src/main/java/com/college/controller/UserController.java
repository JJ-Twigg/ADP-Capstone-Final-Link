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

    // Read a user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<User> read(@PathVariable int userId) {
        User user = userService.read(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    // Update a user
    @PutMapping("/{userId}")
    public ResponseEntity<User> update(@PathVariable int userId, @RequestBody User user) {
        user.setUserId(userId); // ensure the ID matches the path
        User updated = userService.update(user);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    // Delete a user
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable int userId) {
        boolean deleted = userService.delete(userId);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}

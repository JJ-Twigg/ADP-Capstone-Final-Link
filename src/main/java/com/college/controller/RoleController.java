package com.college.controller;

import com.college.domain.Role;
import com.college.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // Get all roles
    @GetMapping
    public List<Role> getAll() {
        return roleService.getAll();
    }

    // Create a new role
    @PostMapping
    public Role create(@RequestBody Role role) {
        return roleService.create(role);
    }

    // Read a role by ID
    @GetMapping("/{id}")
    public ResponseEntity<Role> read(@PathVariable int id) {
        Role role = roleService.read(id);
        if (role != null) {
            return ResponseEntity.ok(role);
        }
        return ResponseEntity.notFound().build();
    }

    // Update a role
    @PutMapping("/{id}")
    public ResponseEntity<Role> update(@PathVariable int id, @RequestBody Role role) {
        role = new Role.RoleBuilder()
                .id(id) // ensure ID from path matches
                .roleName(role.getRoleName())
                .build();

        Role updated = roleService.update(role);
        return ResponseEntity.ok(updated);
    }

    // Delete a role
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean deleted = roleService.delete(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}

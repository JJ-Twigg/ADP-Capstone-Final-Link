package com.college.service;

import com.college.domain.UserRole;
import com.college.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService implements IUserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    // Get all UserRole entries
    @Override
    public List<UserRole> getAll() {
        return userRoleRepository.findAll();
    }

    // Create a new UserRole entry
    @Override
    public UserRole create(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    // Read a UserRole by ID
    @Override
    public UserRole read(Integer id) {
        return userRoleRepository.findById(id).orElse(null);
    }

    // Update an existing UserRole
    @Override
    public UserRole update(UserRole userRole) {
        if (userRoleRepository.existsById(userRole.getId())) {
            return userRoleRepository.save(userRole);
        }
        return null;
    }

    // Delete a UserRole by ID
    @Override
    public boolean delete(Integer id) {
        if (userRoleRepository.existsById(id)) {
            userRoleRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

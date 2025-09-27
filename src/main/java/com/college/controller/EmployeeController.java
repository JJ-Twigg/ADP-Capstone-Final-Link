package com.college.controller;

import com.college.domain.Employee;
import com.college.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Get all employees
    @GetMapping
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    // Create a new employee
    @PostMapping
    public Employee create(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    // Read an employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> read(@PathVariable Integer id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee != null) {
            return ResponseEntity.ok(employee);
        }
        return ResponseEntity.notFound().build();
    }

    // Update an employee
    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(
            @PathVariable Integer id,
            @RequestBody Employee employee
    ) {
        employee.setEmployeeId(id); // ensure the ID matches the path
        Employee updated = employeeRepository.save(employee);
        return ResponseEntity.ok(updated);
    }

    // Delete an employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
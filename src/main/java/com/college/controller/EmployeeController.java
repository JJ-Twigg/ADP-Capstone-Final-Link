package com.college.controller;

import com.college.domain.Employee;
import com.college.domain.subclasses.FoodWorker;
import com.college.repository.EmployeeRepository;
import com.college.service.IFoodWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeRepository repo;


    // Get all food workers
    @GetMapping
    public List<Employee> getAll() {
        return repo.findAll();
    }

    // Create a new food worker
    @PostMapping
    public Employee create(@RequestBody Employee employee) {
        return repo.save(employee);
    }

    // Read a food worker by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> read(@PathVariable Integer id) {
        Employee worker = repo.findById(id).orElse(null);
        if (worker != null) {
            return ResponseEntity.ok(worker);
        }
        return ResponseEntity.notFound().build();
    }

    // Update a food worker
    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(
            @PathVariable Integer id,
            @RequestBody Employee employee
    ) {
        employee.setId(id); // ensure the ID matches the path
        Employee updated = repo.save(employee);
        return ResponseEntity.ok(updated);
    }

    // Delete a food worker
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        repo.deleteById(id);
//        return true;
//
//        if (deleted) {
//            return ResponseEntity.ok().build();
//        }
        return ResponseEntity.notFound().build();
    }
}

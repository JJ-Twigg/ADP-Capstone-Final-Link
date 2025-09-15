package com.college.controller;

import com.college.domain.subclasses.FoodWorker;
import com.college.service.IFoodWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food-workers")
public class FoodWorkerController {
    @Autowired
    private IFoodWorkerService foodWorkerService;


    // Get all food workers
    @GetMapping
    public List<FoodWorker> getAll() {
        return foodWorkerService.getAll();
    }

    // Create a new food worker
    @PostMapping
    public FoodWorker create(@RequestBody FoodWorker foodWorker) {
        return foodWorkerService.create(foodWorker);
    }

    // Read a food worker by ID
    @GetMapping("/{id}")
    public ResponseEntity<FoodWorker> read(@PathVariable Integer id) {
        FoodWorker worker = foodWorkerService.read(id);
        if (worker != null) {
            return ResponseEntity.ok(worker);
        }
        return ResponseEntity.notFound().build();
    }

    // Update a food worker
    @PutMapping("/{id}")
    public ResponseEntity<FoodWorker> update(@PathVariable Integer id, @RequestBody FoodWorker foodWorker) {
        foodWorker.setId(id); // ensure the ID matches the path
        FoodWorker updated = foodWorkerService.update(foodWorker);
        return ResponseEntity.ok(updated);
    }

    // Delete a food worker
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean deleted = foodWorkerService.delete(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}

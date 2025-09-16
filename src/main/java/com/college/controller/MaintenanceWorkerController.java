package com.college.controller;

import com.college.domain.MaintenanceWorker;
import com.college.service.MaintenanceWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MaintenanceWorkerController {

    private final MaintenanceWorkerService service;

    @Autowired
    public MaintenanceWorkerController(MaintenanceWorkerService service) {
        this.service = service;
    }

    @GetMapping("/get/maintenanceworker/{id}")
    public MaintenanceWorker read(@PathVariable int id) {
        return service.findById(id);
    }

    @GetMapping("/get/maintenanceworkers")
    public List<MaintenanceWorker> getAll() {
        return service.getAll();
    }

    @PostMapping("/create/maintenanceworker")
    public MaintenanceWorker create(@RequestBody MaintenanceWorker worker) {
        return service.create(worker);
    }

    @PutMapping("/update/maintenanceworker")
    public MaintenanceWorker update(@RequestBody MaintenanceWorker worker) {
        return service.update(worker);
    }

    @DeleteMapping("/delete/maintenanceworker")
    public void delete(@RequestBody int id) {
        service.delete(id);
    }
}
package com.college.controller;

import com.college.domain.Shift;
import com.college.service.IShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shift")
public class ShiftController {


    private final IShiftService service;

    @Autowired
    public ShiftController(IShiftService shiftService) {
        this.service = shiftService;
    }

    @GetMapping("/get/{id}")
    public Shift read(@PathVariable int id) {
        return service.findById(id).orElse(null);
    }

    @GetMapping("/get/all")
    public List<Shift> getAll() {
        return service.getAll();
    }

    @PostMapping("/create")
    public Shift create(@RequestBody Shift shift) {
        return service.create(shift);
    }

    @PutMapping("/update")
    public Shift update(@RequestBody Shift shift) {
        return service.update(shift);
    }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable int id) {
        service.delete(id);
        return true;
    }

}

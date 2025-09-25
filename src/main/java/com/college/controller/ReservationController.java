/*
File name:  ReservationController.java (Controller Class)
Author:     Ammaar
Started:    20.05.25
*/

package com.college.controller;

import com.college.domain.Guest;
import com.college.domain.Reservation;
import com.college.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReservationController {

    private ReservationService service;

    @Autowired
    public ReservationController(ReservationService reservationService){
        service = reservationService;
    }

    @GetMapping("get/reservation/{id}")
    public Reservation read(@PathVariable int id){
        return service.read(id);
    }

    @GetMapping("/get/reservations")
    public List<Reservation> getAll(){
        return service.getAll();
    }

    @PostMapping("/create")
    public Reservation create(@RequestBody Reservation reservation){
        return service.create(reservation);
    }

    @PutMapping("/update")
    public Reservation update(@RequestBody Reservation reservation){
        return service.update(reservation);
    }

    @DeleteMapping("/delete")
    public boolean delete(@RequestBody int id){
        return service.delete(id);
    }


}

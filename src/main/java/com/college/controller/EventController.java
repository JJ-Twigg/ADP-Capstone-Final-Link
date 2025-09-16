package com.college.controller;

import com.college.domain.Event;
import com.college.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // CREATE
    @PostMapping("/create")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventService.createEvent(event));
    }

    // READ
    @GetMapping("/read/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable int id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/update")
    public ResponseEntity<Event> updateEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventService.updateEvent(event));
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteEvent(@PathVariable int id) {
        return ResponseEntity.ok(eventService.deleteEvent(id));
    }

    // GET ALL
    @GetMapping("/getall")
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }
}
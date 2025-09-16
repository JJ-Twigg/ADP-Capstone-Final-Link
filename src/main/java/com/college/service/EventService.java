package com.college.service;

import com.college.domain.Event;
import com.college.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // CREATE
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    // READ
    public Optional<Event> getEventById(int eventId) {
        return eventRepository.findById(eventId);
    }

    // UPDATE
    public Event updateEvent(Event event) {
        return eventRepository.save(event);
    }

    // DELETE
    public boolean deleteEvent(int eventId) {
        if (eventRepository.existsById(eventId)) {
            eventRepository.deleteById(eventId);
            return true;
        }
        return false;
    }

    // GET ALL
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}
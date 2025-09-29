package com.college.service;

import com.college.domain.Event;
import com.college.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class EventUIServiceNaked {

    private final EventRepository eventRepository;

    public EventUIServiceNaked(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // CREATE
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    // READ
    public Event getEvent(int eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        return event.orElse(null);
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


    @Transactional
    public void deleteByReservationId(int reservationId) {
        eventRepository.deleteByReservationId(reservationId);
    }

    // GET ALL
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}

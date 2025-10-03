package com.college.service;

import com.college.domain.Guest;
import com.college.repository.GuestRepository;

import java.util.List;
import java.util.Optional;

public class GuestUIServiceNaked {

    private final GuestRepository guestRepository;

    public GuestUIServiceNaked(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    // ----------------- CREATE -----------------
    public Guest addGuest(Guest guest) {
        return guestRepository.save(guest);
    }

    // ----------------- READ -----------------
    public Guest getGuest(int guestId) {
        Optional<Guest> guest = guestRepository.findById(guestId);
        return guest.orElse(null); // or throw custom exception
    }

    // ----------------- UPDATE -----------------
    public Guest updateGuest(Guest guest) {
        if (!guestRepository.existsById(guest.getGuestID())) {
            throw new RuntimeException("Guest not found with ID: " + guest.getGuestID());
        }
        return guestRepository.save(guest);
    }

    // ----------------- DELETE -----------------
    public boolean deleteGuest(int guestId) {
        if (guestRepository.existsById(guestId)) {
            guestRepository.deleteById(guestId);
            return true;
        }
        return false;
    }


    public List<Guest> searchGuestByNameAndSurname(String name, String surname) {
        return guestRepository.findByNameAndSurname(name, surname);
    }

    // ----------------- GET ALL -----------------
    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }
}

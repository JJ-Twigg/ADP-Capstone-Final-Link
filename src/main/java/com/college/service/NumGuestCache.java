package com.college.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NumGuestCache {

    // Key: reservationId, Value: number of guests
    private final Map<Integer, Integer> guestCounts = new HashMap<>();

    public void setNumGuests(int reservationId, int numGuests) {
        guestCounts.put(reservationId, numGuests);
    }

    public Integer getNumGuests(int reservationId) {
        return guestCounts.get(reservationId);
    }

    public Map<Integer, Integer> getAll() {
        return guestCounts;
    }

    public void remove(int reservationId) {
        guestCounts.remove(reservationId);
    }

    public void clear() {
        guestCounts.clear();
    }
}

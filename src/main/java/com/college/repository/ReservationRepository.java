/*
File name:  ReservationRepository.java (Repository Class)
Author:     Ammaar
Started:    20.05.25
*/

package com.college.repository;

import com.college.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByGuestGuestID(int guestId);

    @Query("SELECT r FROM Reservation r LEFT JOIN FETCH r.room rm LEFT JOIN FETCH rm.employee")
    List<Reservation> findAllWithRoomAndEmployee();

    @Query("SELECT COUNT(r) FROM Reservation r")
    int countAllReservations();
}

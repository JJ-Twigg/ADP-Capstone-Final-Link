/*
File name:  ReservationRepository.java (Repository Class)
Author:     Ammaar
Started:    20.05.25
*/

package com.college.repository;

import com.college.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query("SELECT COUNT(r) FROM Reservation r")
    int countAllReservations();
}

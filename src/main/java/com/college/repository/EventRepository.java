package com.college.repository;

import com.college.domain.Event;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Event e WHERE e.reservation.reservationId = :reservationId")
    void deleteByReservationId(@Param("reservationId") int reservationId);
}

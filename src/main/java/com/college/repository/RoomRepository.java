package com.college.repository;

import com.college.domain.Reservation;
import com.college.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    List<Room> findByReservation(Reservation reservation);
}

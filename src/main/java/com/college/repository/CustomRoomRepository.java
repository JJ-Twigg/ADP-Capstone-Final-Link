package com.college.repository;

import com.college.domain.Reservation;
import com.college.domain.CustomRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomRoomRepository extends JpaRepository<CustomRoom, Integer> {

    List<CustomRoom> findByReservation(Reservation reservation);
}

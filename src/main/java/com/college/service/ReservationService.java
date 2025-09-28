/*
File name:  ReservationService.java (Service Class)
Author:     Ammaar
Started:    20.05.25
*/

package com.college.service;

import com.college.domain.Reservation;
import com.college.domain.Room;
import com.college.repository.ReservationRepository;
import com.college.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
//import java.util.Optional;


@Service
public class ReservationService {
    private final ReservationRepository repo;
    private final RoomRepository roomRepo;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository){
        this.repo = reservationRepository;
        this.roomRepo = roomRepository;  // ✅ now initialized
    }
    public Reservation create(Reservation reservation){
        return repo.save(reservation);
    }

    public Reservation read(int id){
        return repo.findById(id).orElse(null);
    }

    public Reservation update(Reservation reservation){
        return repo.save(reservation);
    }



    public int getCurrentReservationsCount() {
        return repo.countAllReservations();
    }


    @Transactional
    public boolean delete(int id){
        // 1️⃣ Fetch the managed reservation
        Reservation reservation = repo.findById(id).orElse(null);
        if (reservation == null) return false;

        // 2️⃣ Find rooms linked to this reservation
        List<Room> linkedRooms = roomRepo.findByReservation(reservation);
        for (Room room : linkedRooms) {
            room.setReservation(null);    // unlink reservation
            room.setAvailability(true);   // optional: mark room free
            roomRepo.save(room);
        }

        // 3️⃣ Delete the reservation
        repo.delete(reservation);
        return true;
    }








    public List<Reservation> getAll(){
        return repo.findAll();
    }
    // ---------------------------

}

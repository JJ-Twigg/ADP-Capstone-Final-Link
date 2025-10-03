package com.college.repository;

import com.college.domain.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Integer> {

    //search for a guest
    @Query("SELECT g FROM Guest g WHERE LOWER(g.name) = LOWER(:name) AND LOWER(g.surname) = LOWER(:surname)")
    List<Guest> findByNameAndSurname(@Param("name") String name, @Param("surname") String surname);



}
package com.college.repository;

import com.college.domain.Guest;
import com.college.domain.Payment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Payment p WHERE p.guest.guestID = :guestId")
    void deleteByGuestId(@Param("guestId") int guestId);

    //Find top 3 guests by top revenue, top 3 who paid the highest basically
    @Query("SELECT p.guest FROM Payment p " +
            "GROUP BY p.guest " +
            "ORDER BY SUM(p.paymentAmount) DESC")
    List<Guest> findTopRevenueGeneratingGuests(Pageable pageable);

    List<Payment> findByGuest(Guest guest);


}

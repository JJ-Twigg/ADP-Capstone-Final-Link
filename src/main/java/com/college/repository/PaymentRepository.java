package com.college.repository;

import com.college.domain.Payment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Payment p WHERE p.guest.guestID = :guestId")
    void deleteByGuestId(@Param("guestId") int guestId);
}

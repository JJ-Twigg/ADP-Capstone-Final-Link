package com.college.service;

import com.college.domain.Guest;
import com.college.domain.Payment;
import com.college.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService implements IPaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment create(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Payment update(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public void delete(Integer id) {
        paymentRepository.deleteById(id);
    }

    @Override
    public Optional<Payment> findById(Integer id) {
        return paymentRepository.findById(id);
    }



    public List<Payment> getPaymentsByGuest(Guest guest) {
        return paymentRepository.findByGuest(guest);
    }


    @Transactional
    public void deleteByGuestId(int guestId) {
        paymentRepository.deleteByGuestId(guestId);
    }

    public Double getTotalAmount() {
        return paymentRepository.getTotalAmount();
    }

    @Override
    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }
}

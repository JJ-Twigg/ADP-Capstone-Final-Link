package com.college.service;

import com.college.domain.Shift;
import com.college.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class ShiftService implements IShiftService {
    private final ShiftRepository shiftRepository;

    @Autowired
    public ShiftService(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    public Shift create(Shift shift) {
        return shiftRepository.save(shift);
    }

    public Shift update(Shift shift) {
        return shiftRepository.save(shift);
    }

    @Transactional
    public void delete(Integer id) {
        shiftRepository.findById(id).ifPresent(shift -> {
            // Break reference from parent Employee
            if (shift.getEmployee() != null) {
                shift.getEmployee().setShift(null); // remove reference to allow deletion
            }

            // Delete the shift
            shiftRepository.delete(shift);
            shiftRepository.flush();
        });
    }

    public java.util.Optional<Shift> findById(Integer id) {
        return shiftRepository.findById(id);
    }

    public java.util.List<Shift> getAll() {
        return shiftRepository.findAll();
    }

    @Override
    public boolean existsById(Integer id) {
        return shiftRepository.existsById(id);
    }

    public boolean existsByEmployeeId(int employeeId) {
        return shiftRepository.existsByEmployee_EmployeeId(employeeId);
    }
}
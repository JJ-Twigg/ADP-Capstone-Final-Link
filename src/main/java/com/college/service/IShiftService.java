package com.college.service;

import com.college.domain.Shift;
import java.util.List;
import java.util.Optional;

public interface IShiftService {
    Shift create(Shift shift);
    Shift update(Shift shift);
    void delete(Integer id);
    Optional<Shift> findById(Integer id);
    List<Shift> getAll();
    boolean existsById(Integer id);
}

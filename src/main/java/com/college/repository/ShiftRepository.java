package com.college.repository;

import com.college.domain.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Integer> {
    boolean existsByEmployee_EmployeeId(int employeeId);
}
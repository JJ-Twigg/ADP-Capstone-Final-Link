package com.college.repository;

import com.college.domain.Employee;
import com.college.domain.EmployeeSalary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeSalaryRepository extends JpaRepository<EmployeeSalary, Integer> {
    boolean existsByEmployee_EmployeeId(int employeeId);

    EmployeeSalary findByEmployee(Employee employee);
}

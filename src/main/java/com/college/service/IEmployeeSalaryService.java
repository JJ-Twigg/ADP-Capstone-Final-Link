package com.college.service;

import com.college.domain.EmployeeSalary;
import java.util.List;
import java.util.Optional;


public interface IEmployeeSalaryService {
    EmployeeSalary create(EmployeeSalary employeeSalary);

    EmployeeSalary update(EmployeeSalary employeeSalary);

    void delete(Integer id);

    Optional<EmployeeSalary> findById(Integer id);

    List<EmployeeSalary> getAll();
}

package com.college.service;

import com.college.domain.Employee;
import com.college.domain.EmployeeSalary;
import com.college.repository.EmployeeSalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class EmployeeSalaryService implements IEmployeeSalaryService {

    private final EmployeeSalaryRepository employeeSalaryRepository;

    @Autowired
    public EmployeeSalaryService(EmployeeSalaryRepository employeeSalaryRepository) {
        this.employeeSalaryRepository = employeeSalaryRepository;
    }

    public EmployeeSalary findByEmployee(Employee employee) {
        return employeeSalaryRepository.findByEmployee(employee);
    }

    @Override
    public EmployeeSalary create(EmployeeSalary employeeSalary) {
        return employeeSalaryRepository.save(employeeSalary);
    }

    @Override
    public EmployeeSalary update(EmployeeSalary employeeSalary) {
        return employeeSalaryRepository.save(employeeSalary);
    }

    @Override
    public boolean existsById(Integer id) {
        return employeeSalaryRepository.existsById(id);
    }


    @Override
    public List<EmployeeSalary> getAll() {
        return employeeSalaryRepository.findAll();
    }

    public Optional<EmployeeSalary> findByEmployeeId(int employeeId) {
        return Optional.ofNullable(employeeSalaryRepository.findByEmployee_EmployeeId(employeeId));
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        employeeSalaryRepository.findById(id).ifPresent(salary -> {
            Employee employee = salary.getEmployee();
            if (employee != null) {
                // Break the One-to-One reference from the parent
                employee.setSalary(null);
            }

            // Now delete the salary
            employeeSalaryRepository.delete(salary);
            employeeSalaryRepository.flush();
        });
    }




    @Override
    public Optional<EmployeeSalary> findById(Integer id) {
        return employeeSalaryRepository.findById(id);
    }


    public boolean existsByEmployeeId(int employeeId) {
        return employeeSalaryRepository.existsByEmployee_EmployeeId(employeeId);
    }

    public void deleteByEmployee(Employee employee) {
        if (employee == null) return;

        EmployeeSalary salary = employeeSalaryRepository.findByEmployee(employee);
        if (salary != null) {
            employeeSalaryRepository.delete(salary);
        }
    }




}

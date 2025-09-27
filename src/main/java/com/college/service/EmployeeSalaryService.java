package com.college.service;

import com.college.domain.EmployeeSalary;
import com.college.repository.EmployeeSalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeSalaryService implements IEmployeeSalaryService {

    private final EmployeeSalaryRepository employeeSalaryRepository;

    @Autowired
    public EmployeeSalaryService(EmployeeSalaryRepository employeeSalaryRepository) {
        this.employeeSalaryRepository = employeeSalaryRepository;
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
    public List<EmployeeSalary> getAll() {
        return employeeSalaryRepository.findAll();
    }

    @Override
    public void delete(Integer id) {
        employeeSalaryRepository.deleteById(id);
    }

    @Override
    public Optional<EmployeeSalary> findById(Integer id) {
        return employeeSalaryRepository.findById(id);
    }
}

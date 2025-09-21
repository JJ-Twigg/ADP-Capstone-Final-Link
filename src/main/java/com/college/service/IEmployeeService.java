package com.college.service;

import com.college.domain.Employee;
import com.college.domain.subclasses.FoodWorker;

import java.util.List;

public interface IEmployeeService extends IService<Employee, Integer>{

    List<Employee> getAll();

}

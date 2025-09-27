package com.college.factory;

import com.college.domain.EmployeeSalary;
import java.time.LocalDate;


public class EmployeeSalaryFactory {
    public static EmployeeSalary createEmployeeSalary(int salaryId, double amount, String method, LocalDate date) {
        return new EmployeeSalary.Builder()
                .setSalaryId(salaryId)
                .setAmount(amount)
                .setMethod(method)
                .setDate(date)
                .build();
    }
}

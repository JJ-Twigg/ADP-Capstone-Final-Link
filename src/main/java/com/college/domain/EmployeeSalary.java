package com.college.domain;


import jakarta.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "employee_salary")
public class EmployeeSalary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int salaryId;

    private double amount;
    private String method;
    private LocalDate date;



    //FK to employee
    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employeeId", unique = true)
    private Employee employee;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public EmployeeSalary() {

    }

    public EmployeeSalary(int salaryId, double amount, String method, LocalDate date) {
        this.salaryId = salaryId;
        this.amount = amount;
        this.method = method;
        this.date = date;
    }

    private EmployeeSalary(Builder builder) {
        this.salaryId = builder.salaryId;
        this.amount = builder.amount;
        this.method = builder.method;
        this.date = builder.date;
    }

    public int getSalaryId() { return salaryId; }
    public void setSalaryId(int salaryId) { this.salaryId = salaryId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) {
        this.date = date;
    }

        @Override
        public String toString () {
            return "EmployeeSalary{" +
                    "salaryId=" + salaryId +
                    ", amount=" + amount + '\'' +
                    ", method='" + method + '\'' +
                    ", date=" + date +
                    '}';
        }


        public static class Builder {
            private int salaryId;
            private double amount;
            private String method;
            private LocalDate date;

            public Builder setSalaryId(int salaryId) {
                this.salaryId = salaryId;
                return this;
            }

            public Builder setAmount(double amount) {
                this.amount = amount;
                return this;
            }

            public Builder setMethod(String method) {
                this.method = method;
                return this;
            }

            public Builder setDate(LocalDate date) {
                this.date = date;
                return this;
            }

            public EmployeeSalary build() {
                return new EmployeeSalary(this);
            }

        }
    }





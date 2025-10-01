package com.college.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int employeeId;

    private String jobType;
    private LocalDate startDate;


    //FK TO ROOM
    @OneToOne(mappedBy = "employee")
    private Room room;

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }







    //FK TO USERS TABLE
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId") // FK column
    private User user;

    //FK Parent to shift
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Shift shift;


    // FK Parent to EmployeeSalary
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private EmployeeSalary salary;

    public EmployeeSalary getSalary() {
        return salary;
    }

    public void setSalary(EmployeeSalary salary) {
        this.salary = salary;
    }



    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }






    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    public Employee() {

    }



    //HERE WE SET FK when we make an employee object using its constructor and not factory.
    public Employee(String jobType, LocalDate startDate, User user) {
        this.jobType = jobType;
        this.startDate = startDate;
        this.user = user;
    }





    // Getters
    public int getEmployeeId() {
        return employeeId;
    }

    public String getJobType() {
        return jobType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }



    // Setters
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }



    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", jobType='" + jobType + '\'' +
                ", startDate=" + startDate +
                '}';
    }
}
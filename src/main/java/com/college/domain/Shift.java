/*
File name:  Shift.java
Author:     Talia Smuts
Student Number: 221126082
*/

package com.college.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Table(name = "shift")
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int shiftId;

    private LocalDate shiftDay;
    private LocalTime shiftStartTime;
    private LocalTime shiftEndTime;
    private boolean shiftOvertime;




    //FK to Employee
    @OneToOne
    @JoinColumn(name = "employee_id", unique = true) // foreign key to Employee
    private Employee employee;


    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Shift() {}

    // Full constructor
    public Shift(int shiftId, LocalDate shiftDay, LocalTime shiftStartTime, LocalTime shiftEndTime, boolean shiftOvertime) {
        this.shiftId = shiftId;
        this.shiftDay = shiftDay;
        this.shiftStartTime = shiftStartTime;
        this.shiftEndTime = shiftEndTime;
        this.shiftOvertime = shiftOvertime;
    }

    // Private constructor for Builder
    private Shift(Builder builder) {
        this.shiftId = builder.shiftId;
        this.shiftDay = builder.shiftDay;
        this.shiftStartTime = builder.shiftStartTime;
        this.shiftEndTime = builder.shiftEndTime;
        this.shiftOvertime = builder.shiftOvertime;
    }

    // Getters and Setters
    public int getShiftId() { return shiftId; }
    public void setShiftId(int shiftId) { this.shiftId = shiftId; }

    public LocalDate getShiftDay() { return shiftDay; }
    public void setShiftDay(LocalDate shiftDay) { this.shiftDay = shiftDay; }

    public LocalTime getShiftStartTime() { return shiftStartTime; }
    public void setShiftStartTime(LocalTime shiftStartTime) { this.shiftStartTime = shiftStartTime; }

    public LocalTime getShiftEndTime() { return shiftEndTime; }
    public void setShiftEndTime(LocalTime shiftEndTime) { this.shiftEndTime = shiftEndTime; }

    public boolean getShiftOvertime() { return shiftOvertime; }
    public void setShiftOvertime(boolean shiftOvertime) { this.shiftOvertime = shiftOvertime; }

    @Override
    public String toString() {
        return "Shift{" +
                "shiftId=" + shiftId +
                ", shiftDay=" + shiftDay +
                ", shiftStartTime='" + shiftStartTime + '\'' +
                ", shiftEndTime='" + shiftEndTime + '\'' +
                ", shiftOvertime=" + shiftOvertime +
                '}';
    }

    // Builder pattern
    public static class Builder {
        private int shiftId;
        private LocalDate shiftDay;
        private LocalTime shiftStartTime;
        private LocalTime shiftEndTime;
        private boolean shiftOvertime;

        public Builder setShiftId(int shiftId) {
            this.shiftId = shiftId;
            return this;
        }

        public Builder setShiftDay(LocalDate shiftDay) {
            this.shiftDay = shiftDay;
            return this;
        }

        public Builder setShiftStartTime(LocalTime shiftStartTime) {
            this.shiftStartTime = shiftStartTime;
            return this;
        }

        public Builder setShiftEndTime(LocalTime shiftEndTime) {
            this.shiftEndTime = shiftEndTime;
            return this;
        }

        public Builder setShiftOvertime(boolean shiftOvertime) {
            this.shiftOvertime = shiftOvertime;
            return this;
        }

        public Shift build() {
            return new Shift(this);
        }
    }
}

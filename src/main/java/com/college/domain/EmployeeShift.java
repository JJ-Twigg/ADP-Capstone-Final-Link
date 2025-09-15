package com.college.domain;

import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;

//import java.time.LocalDate;
import java.time.LocalDateTime;

//@Getter
//@Setter
@Entity
public class EmployeeShift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private boolean isOvertime;
//    private String day;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // FK
//    private int employeeId;

    public EmployeeShift(){}
    public EmployeeShift(
            boolean isOvertime,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        this.isOvertime = isOvertime;
        this.startTime = startTime;
        this.endTime = endTime;
    }

//    public int getId() {
//        return id;
//    }
//
//    public boolean isOvertime() {
//        return isOvertime;
//    }
//
//    public LocalDateTime getStartTime() {
//        return startTime;
//    }
//
//    public LocalDateTime getEndTime() {
//        return endTime;
//    }

    @Override
    public String toString() {
        return "EmployeeShift{" +
                "id=" + id +
                ", isOvertime=" + isOvertime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}


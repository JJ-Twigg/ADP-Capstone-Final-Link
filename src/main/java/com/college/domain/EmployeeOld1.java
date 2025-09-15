//package com.college.domain;
//
//import jakarta.persistence.*;
//
//import java.time.LocalTime;
//
////@Getter
////@Setter
//@Entity
//@Table(name="Employee")
//public class EmployeeOld1 {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    private String firstNames;
//    private String lastName;
//    private String gender;
//    private int age;
//    //    private String empContactNum;
////    private String empEmail;
////    private String empAddress; // ?
//
////    private LocalDate empStartDate;
////    private LocalDateTime empStartDate;
//    private LocalTime startDate;
//
//    public EmployeeOld1(){}
//    public EmployeeOld1(String empFirstName, String empLastName, LocalTime date) {
//        this.firstNames = empFirstName;
//        this.lastName = empLastName;
//        this.startDate = date;
//    }
//
////    public Employee(
////            String firstNames,
////            String lastName,
////            String gender,
////            int age,
////            LocalTime empStartDate
////    ) {
////        this.firstNames = firstNames;
////        this.lastName = lastName;
////        this.gender = gender;
////        this.age = age;
////        this.empStartDate = empStartDate;
////    }
//    // -------------------------------------
//
//    // getters
//    public int getId() {
//        return id;
//    }
//
//    public String getFirstNames() {
//        return firstNames;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//
//    public int getAge() {
//        return age;
//    }
//
//    public LocalTime getStartDate() {
//        return startDate;
//    }
//
//    // setters
//    public void setFirstNames(String firstNames) {
//        this.firstNames = firstNames;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public void setGender(String gender) {
//        this.gender = gender;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }
//
//    public void setStartDate(LocalTime startDate) {
//        this.startDate = startDate;
//    }
//}

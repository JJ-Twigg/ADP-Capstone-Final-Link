package com.college.domain;

import com.college.domain.subclasses.FoodWorker;
import jakarta.persistence.*;
import jakarta.persistence.Table;
//import lombok.Getter;
//import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

//@Getter
//@Setter
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstNames;

    private String lastName;

//    private LocalTime startDate;
//    private LocalDateTime registerDateTime;

    private String formattedDateTime;

    private String gender;

    private int age;

    //FK to Shift
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private Shift shift;

    //FK to FoodWorker
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private FoodWorker foodWorker;

    //FK to maintenanceWorker
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private MaintenanceWorker maintenanceWorker;

    //FK to houseKeeper
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private Housekeeper housekeeper;





    public Employee(){}

    public Employee(String empFirstName, String empLastName, String dateTime) {
        this.firstNames = empFirstName;
        this.lastName = empLastName;
//        this.registerDateTime = dateTime;
        this.formattedDateTime = dateTime;
    }



//    public Employee(String empFirstName, String empLastName) {
//        this.firstNames = empFirstName;
//        this.lastName = empLastName;
//    }

//    public Employee(
//            String firstNames,
//            String lastName,
//            String gender,
//            int age,
//            LocalTime empStartDate
//    ) {
//        this.firstNames = firstNames;
//        this.lastName = lastName;
//        this.gender = gender;
//        this.age = age;
//        this.empStartDate = empStartDate;
//    }
    // -------------------------------------

    // getters

    public String getFormattedDateTime() {
        return formattedDateTime;
    }

    public void setFormattedDateTime(String formattedDateTime) {
        this.formattedDateTime = formattedDateTime;
    }

    public int getId() {
        return id;
    }

    public String getFirstNames() {
        return firstNames;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }


//    public LocalDateTime getRegisterDateTime() {
//        return registerDateTime;
//    }

//    public void setRegisterDateTime(LocalDateTime registerDateTime) {
//        this.registerDateTime = registerDateTime;
//    }


    // setters

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstNames(String firstNames) {
        this.firstNames = firstNames;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "formattedDateTime=" + formattedDateTime +
                ", lastName='" + lastName + '\'' +
                ", firstNames='" + firstNames + '\'' +
                ", id=" + id +
                '}';
    }
}

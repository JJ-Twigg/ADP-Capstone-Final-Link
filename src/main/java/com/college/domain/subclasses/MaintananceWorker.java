package com.college.domain.subclasses;

import com.college.domain.Employee;
import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;



//@Getter
//@Setter
//@Entity

@Deprecated
public class MaintananceWorker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private boolean isExternal;

    private String company;

    private String type;


    //FK to Employee
    @OneToOne
    @JoinColumn(name = "employee_id", unique = true) // foreign key to Employee
    private Employee employee;




}

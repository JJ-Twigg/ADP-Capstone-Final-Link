/*
    File name:  Housekeeper.java
    Author:     Muaath Slamong (230074138)
    Date:       22 March 2025
*/


package com.college.domain;

import jakarta.persistence.*;

@Entity
@Table(name="Housekeeper")
public class Housekeeper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int housekeeperId;

    private String housekeeperName;
    private String housekeeperSurname;



    //FK to Employee
    @OneToOne
    @JoinColumn(name = "employee_id", unique = true) // foreign key to Employee
    private Employee employee;







    protected Housekeeper() {}

    private Housekeeper(HousekeeperBuilder builder) {
        this.housekeeperId = builder.housekeeperId;
        this.housekeeperName = builder.housekeeperName;
        this.housekeeperSurname = builder.housekeeperSurname;
    }

    public int getHousekeeperId() {
        return housekeeperId;
    }

    public String getHousekeeperName() {
        return housekeeperName;
    }

    public String getHousekeeperSurname() {
        return housekeeperSurname;
    }

    @Override
    public String toString() {
        return "Housekeeper{" +
                "housekeeperId=" + housekeeperId +
                ", housekeeperName='" + housekeeperName + '\'' +
                ", housekeeperSurname='" + housekeeperSurname + '\'' +
                '}';
    }

    public static class HousekeeperBuilder {
        private int housekeeperId;
        private String housekeeperName;
        private String housekeeperSurname;

        public HousekeeperBuilder setHousekeeperId(int housekeeperId) {
            this.housekeeperId = housekeeperId;
            return this;
        }

        public HousekeeperBuilder setHousekeeperName(String housekeeperName) {
            this.housekeeperName = housekeeperName;
            return this;
        }

        public HousekeeperBuilder setHousekeeperSurname(String housekeeperSurname) {
            this.housekeeperSurname = housekeeperSurname;
            return this;
        }

        public Housekeeper build() {
            return new Housekeeper(this);
        }
    }
}
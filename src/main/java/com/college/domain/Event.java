package com.college.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
//import lombok.Getter;
//import lombok.Setter;

//@Setter
//@Getter
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String eventName;
    private int numberOfPeople;

    // FK
//    private int reservationId;

    public Event(){}
    public Event(String eventName, int numberOfPeople) {
        this.eventName = eventName;
        this.numberOfPeople = numberOfPeople;
    }
    // ----------------------------------

}

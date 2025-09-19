package com.college.domain;

import jakarta.persistence.*;  // Only if you're using JPA/Hibernate, otherwise remove

@Entity
@Table(name="Event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventId;


    private String reason;


    private String description;






    //FK Relationship to reservation
    @OneToOne
    @JoinColumn(name = "reservation_id") // FK column in Event table
    private Reservation reservation;

//getter and setter for gaining access to fk
    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }








    
    // -------- Constructors --------
    public Event() {}
    public Event(int eventId, String reason, String description) {
        this.eventId = eventId;
        this.reason = reason;
        this.description = description;
    }

    // -------- Getters & Setters --------
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // -------- toString --------
    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", reason='" + reason + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
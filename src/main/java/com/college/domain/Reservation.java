/*
File name:  Reservation.java (Model/Entity Class)
Author:     Ammaar
Started:    12.03.25
*/

package com.college.domain;

import jakarta.persistence.*;

@Entity
@Table(name="Reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservationId;
    private String reservationDateTimeStart;
    private String reservationDateTimeEnd;
    // -----------------------------------






    //FK Relationship to Room
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Room room;

    //FK Relationship to event
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Event event;




    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }





    public Reservation(){}
    public Reservation(String reservationDateTimeStart, String reservationDateTimeEnd) {
        this.reservationDateTimeStart = reservationDateTimeStart;
        this.reservationDateTimeEnd = reservationDateTimeEnd;
    }
    private Reservation(Builder reservationBuilder){
        this.reservationId = reservationBuilder.builderId;
        this.reservationDateTimeStart = reservationBuilder.builderDateTimeStart;
        this.reservationDateTimeEnd = reservationBuilder.builderDateTimeEnd;
    }
    // -----------------------------------

    // getters


    public String getReservationDateTimeStart() {
        return reservationDateTimeStart;
    }

    public String getReservationDateTimeEnd() {
        return reservationDateTimeEnd;
    }

    public void setReservationDateTimeStart(String reservationDateTimeStart) {
        this.reservationDateTimeStart = reservationDateTimeStart;
    }

    public void setReservationDateTimeEnd(String reservationDateTimeEnd) {
        this.reservationDateTimeEnd = reservationDateTimeEnd;
    }

    @Override
    public String toString() {
        if (this.reservationId == 0){
            return null;
        }

        return "Reservation{" +
                "reservationId=" + reservationId +
                ", reservationDateTimeStart='" + reservationDateTimeStart + '\'' +
                ", reservationDateTimeEnd='" + reservationDateTimeEnd + '\'' +
                '}';
    }
    // -----------------------------------

    public static class Builder{
        private int builderId;
        private String builderDateTimeStart;
        private String builderDateTimeEnd;

        public Builder(){}
        public Builder(
                int id,
                String dateTimeStart,
                String dateTimeEnd
        ){
            this.builderId = id;
            this.builderDateTimeStart = dateTimeStart;
            this.builderDateTimeEnd = dateTimeEnd;
        }
        // ---------------------------

        public Builder setReservationId(int reservationId) {
            this.builderId = reservationId;
            return this;
        }
        public Builder setReservationDateTimeStart(String reservationDateTimeStart) {
            this.builderDateTimeStart = reservationDateTimeStart;
            return this;
        }
        public Builder setReservationDateTimeEnd(String reservationDateTimeEnd) {
            this.builderDateTimeEnd = reservationDateTimeEnd;
            return this;
        }
        // ---------------------------

        public Builder copy(Reservation reservation){
            this.builderId = reservation.getReservationId();
            this.builderDateTimeEnd = reservation.getReservationDateTimeEnd();
            this.builderDateTimeStart = reservation.getReservationDateTimeStart();
            return this;
        }

        public Reservation build(){
            return new Reservation(this);
        }
    }
}

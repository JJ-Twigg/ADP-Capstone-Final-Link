/* Room.java
Room Model class
Author: joshua twigg (222153881)
Date: 27 March 2025
*/
package com.college.domain;

import jakarta.persistence.*;

@Entity
@Table(name="Room")
public class Room {

    @Id
    private int roomID;

    private String roomType;
    private float pricePerNight;
    private Boolean availability;
    private String features;






    // FK to Reservation
    @OneToOne
    @JoinColumn(name = "reservation_id", referencedColumnName = "reservationId", nullable = true)
    private Reservation reservation;




    // FK to Employee, one to one means only one housekeeper per 1 room
    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employeeId", nullable = true)
    private Employee employee;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }









    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }



    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }





    public Room(){}
    public Room(int roomID, String roomType, float pricePerNight, Boolean availability, String features) {
        this.roomID = roomID;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.availability = availability;
        this.features = features;
    }

    /// Builder Constructor
    private Room(RoomBuilder builder){
        this.roomID = builder.roomID;
        this.roomType = builder.roomType;
        this.pricePerNight = builder.pricePerNight;
        this.availability = builder.availability;
        this.features = builder.features;
    }

    public int getRoomID() {
        return roomID;
    }

    public String getRoomType() {
        return roomType;
    }

    public float getPricePerNight() {
        return pricePerNight;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public String getFeatures() {
        return features;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomID='" + roomID + '\'' +
                ", roomType='" + roomType + '\'' +
                ", pricePerNight=" + pricePerNight +
                ", availability=" + availability +
                ", features='" + features + '\'' +
                '}';
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setPricePerNight(float pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    /// Builder pattern
    public static class RoomBuilder{
        private int roomID;
        private String roomType;
        private float pricePerNight;
        private Boolean availability;
        private String features;

        public RoomBuilder(int roomID, String roomType, float pricePerNight, Boolean availability, String features) {
            this.roomID = roomID;
            this.roomType = roomType;
            this.pricePerNight = pricePerNight;
            this.availability = availability;
            this.features = features;
        }

        public static RoomBuilder copy(Room room) {
            return new RoomBuilder(
                    room.getRoomID(),
                    room.getRoomType(),
                    room.getPricePerNight(),
                    room.getAvailability(),
                    room.getFeatures()
            );
        }

        public RoomBuilder setRoomID(int  roomID) {
            this.roomID = roomID;
            return this;
        }

        public RoomBuilder setRoomType(String roomType) {
            this.roomType = roomType;
            return this;
        }

        public RoomBuilder  setPricePerNight(float pricePerNight) {
            this.pricePerNight = pricePerNight;
            return this;
        }

        public RoomBuilder  setAvailability(Boolean availability) {
            this.availability = availability;
            return this;
        }

        public RoomBuilder  setFeatures(String features) {
            this.features = features;
            return this;
        }

        // build method
        public Room build(){
            return new Room(this);
        }
    }
}

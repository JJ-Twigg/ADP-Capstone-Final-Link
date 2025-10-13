/* CustomRoom.java
Room Model class with image support
Author: joshua twigg (222153881)
Date: 27 March 2025
*/
package com.college.domain;

import jakarta.persistence.*;

@Entity
@Table(name="CustomRoom")
public class CustomRoom {

    @Id
    private int roomID;

    private String roomType;
    private float pricePerNight;
    private Boolean availability;
    private String features;

    // New image attribute for DB storage
    @Lob
    private byte[] image;

    // FK to Reservation
    @OneToOne
    @JoinColumn(name = "reservation_id", referencedColumnName = "reservationId", nullable = true)
    private Reservation reservation;

    public Reservation getReservation() {
        return reservation;
    }

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

    public CustomRoom(){}

    public CustomRoom(int roomID, String roomType, float pricePerNight, Boolean availability, String features, byte[] image) {
        this.roomID = roomID;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.availability = availability;
        this.features = features;
        this.image = image;
    }

    /// Builder Constructor
    private CustomRoom(CustomRoomBuilder builder){
        this.roomID = builder.roomID;
        this.roomType = builder.roomType;
        this.pricePerNight = builder.pricePerNight;
        this.availability = builder.availability;
        this.features = builder.features;
        this.image = builder.image;
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

    public byte[] getImage() {
        return image;
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

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "CustomRoom{" +
                "roomID='" + roomID + '\'' +
                ", roomType='" + roomType + '\'' +
                ", pricePerNight=" + pricePerNight +
                ", availability=" + availability +
                ", features='" + features + '\'' +
                '}';
    }

    /// Builder pattern
    public static class CustomRoomBuilder{
        private int roomID;
        private String roomType;
        private float pricePerNight;
        private Boolean availability;
        private String features;
        private byte[] image;

        public CustomRoomBuilder(int roomID, String roomType, float pricePerNight, Boolean availability, String features, byte[] image) {
            this.roomID = roomID;
            this.roomType = roomType;
            this.pricePerNight = pricePerNight;
            this.availability = availability;
            this.features = features;
            this.image = image;
        }

        public static CustomRoomBuilder copy(CustomRoom room) {
            return new CustomRoomBuilder(
                    room.getRoomID(),
                    room.getRoomType(),
                    room.getPricePerNight(),
                    room.getAvailability(),
                    room.getFeatures(),
                    room.getImage()
            );
        }

        public CustomRoomBuilder setRoomID(int  roomID) {
            this.roomID = roomID;
            return this;
        }

        public CustomRoomBuilder setRoomType(String roomType) {
            this.roomType = roomType;
            return this;
        }

        public CustomRoomBuilder setPricePerNight(float pricePerNight) {
            this.pricePerNight = pricePerNight;
            return this;
        }

        public CustomRoomBuilder setAvailability(Boolean availability) {
            this.availability = availability;
            return this;
        }

        public CustomRoomBuilder setFeatures(String features) {
            this.features = features;
            return this;
        }

        public CustomRoomBuilder setImage(byte[] image) {
            this.image = image;
            return this;
        }

        // build method
        public CustomRoom build(){
            return new CustomRoom(this);
        }
    }
}

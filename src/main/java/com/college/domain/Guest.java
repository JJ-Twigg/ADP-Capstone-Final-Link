
/* Guest.java
Guest Model Class
Author: Zaid Theunissen (221084142)
Date: 26 March 2025
*/

package com.college.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int guestID;

    private String name;
    private String surname;
    private String contactNumber;
    private String email;
    private String paymentDetails;

    protected Guest() {}
    private Guest(GuestBuilder builder) {
        this.paymentDetails = builder.paymentDetails;
        this.email = builder.email;
        this.contactNumber = builder.contactNumber;
        this.surname = builder.surname;
        this.name = builder.name;
        this.guestID = builder.guestID;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public String getEmail() {
        return email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public int getGuestID() {
        return guestID;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "guestID=" + guestID +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", contactNumber=" + contactNumber +
                ", email='" + email + '\'' +
                ", paymentDetails='" + paymentDetails + '\'' +
                '}';
    }

    public static class GuestBuilder {
        private int guestID;
        private String name;
        private String surname;
        private String contactNumber;
        private String email;
        private String paymentDetails;

        public GuestBuilder setGuestID(int guestID) {
            this.guestID = guestID;
            return this;
        }

        public GuestBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public GuestBuilder setSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public GuestBuilder setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
            return this;
        }

        public GuestBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public GuestBuilder setPaymentDetails(String paymentDetails) {
            this.paymentDetails = paymentDetails;
            return this;
        }

        public GuestBuilder copy(Guest guest) {
            this.guestID = guestID;
            this.name = name;
            this.surname = surname;
            this.contactNumber = contactNumber;
            this.email = email;
            this.paymentDetails = paymentDetails;
            return this;
        }

        public Guest build() {
            return new Guest(this);
        }
    }
}

///*
//File name:  Payment.java
//Author:     Talia Smuts
//Student Number: 221126082
//*/
//
//package com.college.domain;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name="Payment")
//public class Payment {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int paymentId;
//
//    private String paymentAmount;
//    private String paymentMethod;
//    private String paymentStatus;
//    private String paymentDate;
//
//
//    public Payment() {}
//    public Payment(int paymentId, String paymentAmount, String paymentMethod, String paymentStatus, String paymentDate) {
//        this.paymentId = paymentId;
//        this.paymentAmount = paymentAmount;
//        this.paymentMethod = paymentMethod;
//        this.paymentStatus = paymentStatus;
//        this.paymentDate = paymentDate;
//    }
//    private Payment(Builder builder) {
//        this.paymentId = builder.paymentId;
//        this.paymentAmount = builder.paymentAmount;
//        this.paymentMethod = builder.paymentMethod;
//        this.paymentStatus = builder.paymentStatus;
//        this.paymentDate = builder.paymentDate;
//    }
//
//
//    public int getPaymentId() {
//        return paymentId;
//    }
//
//    public String getPaymentAmount() {
//        return paymentAmount;
//    }
//
//    public String getPaymentMethod() {
//        return paymentMethod;
//    }
//
//    public String getPaymentStatus() {
//        return paymentStatus;
//    }
//
//    public String getPaymentDate() {
//        return paymentDate;
//    }
//
//    @Override
//    public String toString() {
//        return "Payment{" +
//                "paymentId=" + paymentId +
//                ", paymentAmount='" + paymentAmount + '\'' +
//                ", paymentMethod='" + paymentMethod + '\'' +
//                ", paymentStatus='" + paymentStatus + '\'' +
//                ", paymentDate='" + paymentDate + '\'' +
//                '}';
//    }
//
//
//    public static class Builder {
//        private int paymentId;
//        private String paymentAmount;
//        private String paymentMethod;
//        private String paymentStatus;
//        private String paymentDate;
//
//        public Builder setPaymentId(int paymentId) {
//            this.paymentId = paymentId;
//            return this;
//        }
//
//        public Builder setPaymentAmount(String paymentAmount) {
//            this.paymentAmount = paymentAmount;
//            return this;
//        }
//
//        public Builder setPaymentMethod(String paymentMethod) {
//            this.paymentMethod = paymentMethod;
//            return this;
//        }
//
//        public Builder setPaymentStatus(String paymentStatus) {
//            this.paymentStatus = paymentStatus;
//            return this;
//        }
//
//        public Builder setPaymentDate(String paymentDate) {
//            this.paymentDate = paymentDate;
//            return this;
//        }
//
//        public Payment build() {
//            return new Payment(this);
//        }
//    }
//}


/*
File name:  Payment.java
Author:     Talia Smuts
Student Number: 221126082
*/

package com.college.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="Payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;

    private double paymentAmount;
    private String paymentMethod;
    private String paymentStatus;
    private LocalDate paymentDate;



    //FK To Guest
    @ManyToOne(fetch = FetchType.LAZY)  // Many payments can belong to one guest
    @JoinColumn(name = "guest_id", nullable = false) // FK column
    private Guest guest;


    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }



    public Payment() {}

    // Full constructor
    public Payment(int paymentId, double paymentAmount, String paymentMethod, String paymentStatus, LocalDate paymentDate) {
        this.paymentId = paymentId;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
    }

    // Private constructor for Builder
    private Payment(Builder builder) {
        this.paymentId = builder.paymentId;
        this.paymentAmount = builder.paymentAmount;
        this.paymentMethod = builder.paymentMethod;
        this.paymentStatus = builder.paymentStatus;
        this.paymentDate = builder.paymentDate;
    }

    // Getters and Setters
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public double getPaymentAmount() { return paymentAmount; }
    public void setPaymentAmount(double paymentAmount) { this.paymentAmount = paymentAmount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", paymentAmount=" + paymentAmount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", paymentDate=" + paymentDate +
                '}';
    }

    // Builder pattern
    public static class Builder {
        private int paymentId;
        private double paymentAmount;
        private String paymentMethod;
        private String paymentStatus;
        private LocalDate paymentDate;

        public Builder setPaymentId(int paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public Builder setPaymentAmount(double paymentAmount) {
            this.paymentAmount = paymentAmount;
            return this;
        }

        public Builder setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
            return this;
        }

        public Builder setPaymentDate(LocalDate paymentDate) {
            this.paymentDate = paymentDate;
            return this;
        }

        public Payment build() {
            return new Payment(this);
        }
    }
}

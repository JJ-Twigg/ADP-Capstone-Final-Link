//
////package com.college.models.subclasses;
////
////import jakarta.persistence.Entity;
//
////@Entity
////public class RoomServiceWorker {
////    private int employeeId;
////    private int roomId;
////}
//
//package com.college.domain.subclasses;
//
//import com.college.domain.Employee;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Table;
//import org.springframework.data.annotation.Id;
//
//@Entity
////@Table(name = "room_service_worker")
//public class RoomServiceWorker {
////public class RoomServiceWorker extends Employee {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
////    private int id;
//
//    private String employeeId;
//    private String roomId;
//
//    // FK
////    private int employeeId;
////    private int roomId;
//
//    public RoomServiceWorker(){}
//    public RoomServiceWorker(String employeeId, String roomId) {
//        this.employeeId = employeeId;
//        this.roomId = roomId;
//    }
//
//
//    // Getters
//    public String getEmployeeId() {
//        return employeeId;
//    }
//
//    public String getRoomId() {
//        return roomId;
//    }
//
//    // Setters
//    public void setEmployeeId(String employeeId) {
//        this.employeeId = employeeId;
//    }
//
//    public void setRoomId(String roomId) {
//        this.roomId = roomId;
//    }
//
//    @Override
//    public String toString() {
//        return "RoomServiceWorker{" +
//                "employeeId='" + employeeId + '\'' +
//                ", roomId='" + roomId + '\'' +
//                '}';
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Long getId() {
//        return id;
//    }
//}

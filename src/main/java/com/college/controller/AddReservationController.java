package com.college.controller;

import com.college.MainFinal;
import com.college.domain.*;
import com.college.service.CustomRoomService;
import com.college.service.EmployeeService;
import com.college.service.ReservationService;
import com.college.service.RoomService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;

@Component
public class AddReservationController {

    private Stage parentStage;

    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }


    private Reservation savedReservation;
    private Guest guest;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;

        this.stage.setOnCloseRequest(event -> {
            event.consume(); // stop the window from closing
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Action Blocked");
            alert.setHeaderText(null);
            alert.setContentText("You cannot close this window until a reservation is made.");
            alert.showAndWait();
        });
    }


    @Autowired
    private RoomService roomService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CustomRoomService customRoomService;

    private final ReservationService reservationService;

//    @FXML private TextField startTimeField;
//    @FXML private TextField endTimeField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML private ComboBox<Integer> comboBoxNumbers;
    @FXML private ComboBox<String> comboBoxBookingType;
    @FXML private ComboBox<Employee> comboBoxEmployee;

    @Autowired
    public AddReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }







    @FXML
    public void initialize() {
        // Populate room numbers (51-59)
        for (int i = 51; i <= 65; i++) {
            comboBoxNumbers.getItems().add(i);
        }
        comboBoxNumbers.setValue(52);

        // Booking type
        comboBoxBookingType.getItems().addAll("Room", "Event");
        comboBoxBookingType.setOnAction(e -> {
            String type = comboBoxBookingType.getValue();
            comboBoxNumbers.setVisible(!"Event".equals(type));
            comboBoxEmployee.setVisible(!"Event".equals(type));
        });

        // Employee ComboBox, this just gets all employees and works well.
//        comboBoxEmployee.getItems().addAll(employeeService.getAllEmployees());

        //employee Combo box, get all employees, who are housekeepers
        comboBoxEmployee.getItems().addAll(
                employeeService.getAllEmployees().stream()
                        .filter(emp -> "housekeeper".equalsIgnoreCase(emp.getJobType()))
                        .filter(emp -> roomService.getAll().stream()
                                .noneMatch(room -> room.getEmployee() != null &&
                                        room.getEmployee().getEmployeeId() == emp.getEmployeeId()))
                        .filter(emp -> customRoomService.getAll().stream()
                                .noneMatch(customRoom -> customRoom.getEmployee() != null &&
                                        customRoom.getEmployee().getEmployeeId() == emp.getEmployeeId()))
                        .toList()
        );

        comboBoxEmployee.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Employee emp, boolean empty) {
                super.updateItem(emp, empty);
                setText(empty || emp == null ? "" : emp.getJobType() + " - " + emp.getEmployeeId());
            }
        });
        comboBoxEmployee.setButtonCell(comboBoxEmployee.getCellFactory().call(null));
    }

    @FXML
    private void saveReservation() {

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            System.out.println("Please select both dates.");
            return;
        }

// Convert to String (yyyy-MM-dd format)
        String startTime = startDate.toString();
        String endTime = endDate.toString();

        Integer roomChosen = comboBoxNumbers.getValue();

        //custom room booking
        if (roomChosen != null && roomChosen == 60) {
            boolean customRoomExists = customRoomService.getAll().stream()
                    .anyMatch(customRoom -> customRoom.getRoomID() == 60);

            if (!customRoomExists) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Unavailable Room");
                alert.setHeaderText(null);
                alert.setContentText("Room 60 is not available — it’s not registered in Custom Rooms.");
                alert.showAndWait();
                return;
            } else {
                System.out.println("Room 60 found in CustomRoom table");


            }
        }

        float price = (float) getRoomPrice(roomChosen);
        System.out.println("price of that room id is: " + price);

        String bookingTypeSelected = comboBoxBookingType.getValue();

        if (bookingTypeSelected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a booking type.");
            alert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Reservation");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to save this reservation?");
        if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return; // User clicked Cancel, stop saving
        }

        try {
            if ("Event".equals(bookingTypeSelected)) {
                Reservation reservation = new Reservation(startTime, endTime);
                reservation.setGuest(this.guest);
                savedReservation = reservationService.create(reservation);

                System.out.println("Reservation ID (Event): " + savedReservation.getReservationId());

                // Open Event modal WITHOUT closing parent
                openAddEventDialog(savedReservation);

                // Only close AddReservation modal
                if (stage != null) stage.close();

                // Parent Reservation page remains open
            } else if ("Room".equals(bookingTypeSelected)) {
                if (roomChosen == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Missing Selection");
                    alert.setHeaderText(null);
                    alert.setContentText("Please select a Room.");
                    alert.showAndWait();
                    return;
                }

                Employee chosenEmployee = comboBoxEmployee.getValue();
                if (chosenEmployee == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Missing Selection");
                    alert.setHeaderText(null);
                    alert.setContentText("Please select a housekeeper before saving the reservation.");
                    alert.showAndWait();
                    return;
                }

                Room roomToUpdate = roomService.read(roomChosen);






                // CUSTOM ROOM BOOKING code (60-65)
                if (roomChosen != null && roomChosen >= 60 && roomChosen <= 65) {
                    CustomRoom customRoom = customRoomService.read(roomChosen);

                    if (customRoom == null) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Unavailable Room");
                        alert.setHeaderText(null);
                        alert.setContentText("Room " + roomChosen + " is not available — it’s not registered in Custom Rooms.");
                        alert.showAndWait();
                        return;
                    }

                    // Check if already reserved
                    if (!customRoom.getAvailability()) { // or customRoom.getReservation() != null
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Room Already Reserved");
                        alert.setHeaderText(null);
                        alert.setContentText("Custom Room " + roomChosen + " is already reserved. Please select another room.");
                        alert.showAndWait();
                        return;
                    }

                    // If available, proceed with reservation
                    try {
                        Reservation reservation = new Reservation(startTime, endTime);
                        reservation.setGuest(this.guest);
                        Reservation savedReservation = reservationService.create(reservation);

                        customRoom.setReservation(savedReservation);
                        customRoom.setEmployee(chosenEmployee);
                        customRoom.setAvailability(false); // mark as booked
                        customRoomService.update(customRoom);

                        alertReservationSuccess(roomChosen);

                        double pricee = getCustomRoomPrice(roomChosen);
                        openAddPaymentPage(this.guest, pricee);

                        if (stage != null) stage.close();
                        if (parentStage != null) parentStage.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Error saving custom room reservation: " + e.getMessage());
                    }
                }












                if (roomChosen != null && roomChosen <= 59) {
                    if (Boolean.TRUE.equals(roomToUpdate.getAvailability())) {
                        roomToUpdate.setAvailability(false);

                        Reservation reservation = new Reservation(startTime, endTime);
                        reservation.setGuest(this.guest);
                        savedReservation = reservationService.create(reservation);

                        roomToUpdate.setReservation(savedReservation);
                        roomToUpdate.setEmployee(chosenEmployee);
                        roomService.update(roomToUpdate);

                        alertReservationSuccess(roomChosen);

                        // Open Payment modal
                        openAddPaymentPage(this.guest, price);

                        // Close AddReservation modal and parent Reservation page
                        if (stage != null) stage.close();
                        if (parentStage != null) parentStage.close();

                    } else {
                        alertRoomTaken();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error saving reservation: " + e.getMessage());
        }
    }


    private void alertRoomTaken() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Room Status");
        alert.setHeaderText(null);
        alert.setContentText("Room already taken, please book another");
        alert.showAndWait();
    }

    private void alertReservationSuccess(int roomNum) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reservation Successful");
        alert.setHeaderText(null);
        alert.setContentText("Reservation ID " + roomNum + " saved successfully!");
        alert.showAndWait();
    }

    private void openAddEventDialog(Reservation reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/eventFinal.fxml"));
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean);

            Parent root = loader.load();
            EventUIController eventController = loader.getController();
            eventController.setReservation(reservation);

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Add Event for Reservation ID: " + reservation.getReservationId());
            modalStage.setScene(new Scene(root));
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openAddPaymentPage(Guest guest, double price) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/paymentFinal.fxml"));
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean);

            Parent root = loader.load();
            PaymentViewController paymentController = loader.getController();
            paymentController.setGuest(guest);
            //pass price to setter
            paymentController.setPrice(price);

            Stage modalStage = new Stage();
            modalStage.setWidth(1000);
            modalStage.setHeight(600);
            modalStage.initModality(Modality.APPLICATION_MODAL);

            modalStage.setScene(new Scene(root));
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getRoomPrice(int roomID) {
        try {
            // If it's the custom room, normal room price should be 0
            if (roomID == 60) {
                return 0.0;
            }

            Room room = roomService.read(roomID); // Fetch normal room by ID
            if (room == null) {
                System.out.println("Room with ID " + roomID + " not found!");
                return 0.0;
            }

            double price = room.getPricePerNight();
            System.out.println("Received price from database for room ID " + roomID + ": " + price);
            return price;

        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }


    public double getCustomRoomPrice(int customRoomID) {
        try {
            CustomRoom customRoom = customRoomService.read(customRoomID);
            if (customRoom != null) {
                double price = customRoom.getPricePerNight();
                System.out.println("Received price from CustomRoom: " + price);
                return price;
            } else {
                System.out.println("CustomRoom with ID " + customRoomID + " not found!");
                return 0.0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }



    @FXML
    private void cancel() {
        stage.close();
    }
}

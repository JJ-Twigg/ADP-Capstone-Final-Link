package com.college.controller;

import com.college.MainFinal;
import com.college.domain.Employee;
import com.college.domain.Guest;
import com.college.domain.Reservation;
import com.college.domain.Room;
import com.college.service.EmployeeService;
import com.college.service.ReservationService;
import com.college.service.RoomService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AddReservationController {

    private Reservation savedReservation;
    private Guest guest;
    private Stage stage;

    @Autowired
    private RoomService roomService;

    @Autowired
    private EmployeeService employeeService;

    private final ReservationService reservationService;

    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;
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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        // Populate room numbers (51-59)
        for (int i = 52; i <= 59; i++) {
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

        // Employee ComboBox
        comboBoxEmployee.getItems().addAll(employeeService.getAllEmployees());
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
        String startTime = startTimeField.getText();
        String endTime = endTimeField.getText();

        if (startTime.isEmpty() || endTime.isEmpty()) {
            System.out.println("Please fill in all fields.");
            return;
        }

        Integer roomChosen = comboBoxNumbers.getValue();
        String bookingTypeSelected = comboBoxBookingType.getValue();

        if (bookingTypeSelected == null) {
            System.out.println("Please select a booking type.");
            return;
        }

        try {
            if ("Event".equals(bookingTypeSelected)) {
                Reservation reservation = new Reservation(startTime, endTime);
                reservation.setGuest(this.guest);
                savedReservation = reservationService.create(reservation);

                System.out.println("Reservation ID (Event): " + savedReservation.getReservationId());

                openAddEventDialog(savedReservation);



                stage.close();

            } else if ("Room".equals(bookingTypeSelected)) {
                if (roomChosen == null) {
                    System.out.println("Please select a room.");
                    return;
                }

                Room roomToUpdate = roomService.read(roomChosen);

                if (Boolean.TRUE.equals(roomToUpdate.getAvailability())) {
                    roomToUpdate.setAvailability(false);

                    Reservation reservation = new Reservation(startTime, endTime);
                    reservation.setGuest(this.guest);
                    savedReservation = reservationService.create(reservation);

                    // Link reservation to room and save
                    roomToUpdate.setReservation(savedReservation);
                    roomService.update(roomToUpdate);

                    alertReservationSuccess(roomChosen);
                    openAddPaymentPage(this.guest);

                    stage.close();
                } else {
                    alertRoomTaken();
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

    private void openAddPaymentPage(Guest guest) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/paymentFinal.fxml"));
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean);

            Parent root = loader.load();
            PaymentViewController paymentController = loader.getController();
            paymentController.setGuest(guest);

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

    @FXML
    private void cancel() {
        stage.close();
    }
}

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







    @FXML
    public void initialize() {
        // Populate room numbers (51-59)
        for (int i = 51; i <= 56; i++) {
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

        String startTime = startTimeField.getText();
        String endTime = endTimeField.getText();

        if (startTime.isEmpty() || endTime.isEmpty()) {
            System.out.println("Please fill in all fields.");
            return;
        }

        Integer roomChosen = comboBoxNumbers.getValue();

        float price = (float) getRoomPrice(roomChosen);
        System.out.println("price of that room id is: " + price);

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

                // Open Event modal WITHOUT closing parent
                openAddEventDialog(savedReservation);

                // Only close AddReservation modal
                if (stage != null) stage.close();

                // Parent Reservation page remains open
            } else if ("Room".equals(bookingTypeSelected)) {
                if (roomChosen == null) {
                    System.out.println("Please select a room.");
                    return;
                }

                Employee chosenEmployee = comboBoxEmployee.getValue();
                Room roomToUpdate = roomService.read(roomChosen);

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
            Room room = roomService.read(roomID); // Fetch room by ID
            float price = room.getPricePerNight();
            System.out.println("recieved price from database for that id: " + price);
                return price;




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

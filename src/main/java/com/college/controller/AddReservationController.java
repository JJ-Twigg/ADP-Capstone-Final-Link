package com.college.controller;

import com.college.MainFinal;
import com.college.domain.Reservation;
import com.college.domain.Room;
import com.college.service.ReservationService;
import com.college.service.RoomService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AddReservationController {

    @Autowired
    private RoomService roomService;

    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;

    @FXML
    private ComboBox<Integer> comboBoxNumbers;

    @FXML
    private ComboBox<String> comboBoxRoomType;

    private final ReservationService reservationService;
    private Stage stage;

    @Autowired
    public AddReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        // Populate ComboBox with numbers 51-59
        for (int i = 52; i <= 59; i++) {
            comboBoxNumbers.getItems().add(i);
        }
        comboBoxNumbers.setValue(52); // default value


        // Room Type ComboBox
        comboBoxRoomType.getItems().addAll("Food", "Maintenance", "Housekeeping");
        comboBoxRoomType.setValue("Food"); // default value
    }






    //save reservation with event window coming after
    @FXML
    private void saveReservation() {
        String startTime = startTimeField.getText();
        String endTime = endTimeField.getText();

        if (!startTime.isEmpty() && !endTime.isEmpty()) {
            Reservation newReservation = new Reservation(startTime, endTime);

            //1 GET THE VALUE OF THE PK AUTO GEN, save to variable. so that it can be used with event controller as fk
            //2 run event window
            Reservation savedReservation = reservationService.create(newReservation);
            System.out.println("Reservation ID (FK for Event): " + savedReservation.getReservationId());

            System.out.println("New reservation saved: " + newReservation);

            //for now these are the 2 method calls for fk working.
            //waiting for activity flow which will decide if someone says event, call event fk method page otherwise no event and call room only

            //---ammar needs to add id to his reservation, so we know which room to book!

            //            int roomChosen = 54;

            Integer roomChosen = comboBoxNumbers.getValue();
            if (roomChosen == null) {
                System.out.println("Please select a room.");
                return;
            }






            Room roomToUpdate = roomService.read(roomChosen);



            roomToUpdate.setReservation(savedReservation);

            //if room is available make it unavailable otherwise print its already taken and cant be booked
            if (Boolean.TRUE.equals(roomToUpdate.getAvailability())) {
                // Room is available, so toggle to unavailable
                roomToUpdate.setAvailability(false);
                roomService.update(roomToUpdate);
            } else {
                System.out.println("Room already taken");
                alertRoomTaken();
            }


            roomService.update(roomToUpdate);

            // Mark the room as unavailable


//            openRoomDialog(reservationId);

            //WORKING ROOM CODE
//            saveReservationNoEvent(savedReservation.getReservationId());

            //WORKING EVENT CODE
            openAddEventDialog(savedReservation.getReservationId());

            stage.close();
        }
        else {
            System.out.println("Please fill in all fields.");
        }
    }

    public void alertRoomTaken(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Room Status");
        alert.setHeaderText(null);
        alert.setContentText("Room already taken");
        alert.showAndWait();
    }




//not needed cause we dont need to see the room list afterwards necessarily

//    //save reservation with event window coming after
//    @FXML
//    private void saveReservationNoEvent(Integer reservationId) {
//        String startTime = startTimeField.getText();
//        String endTime = endTimeField.getText();
//
//        if (!startTime.isEmpty() && !endTime.isEmpty()) {
//            Reservation newReservation = new Reservation(startTime, endTime);
//
//            //1 GET THE VALUE OF THE PK AUTO GEN, save to variable. so that it can be used with event controller as fk
//            //2 run event window
//            Reservation savedReservation = reservationService.create(newReservation);
//            System.out.println("Reservation ID (FK for Event): " + savedReservation.getReservationId());
//
//            System.out.println("New reservation saved: " + newReservation);
//
//
//
//            openRoomDialog(savedReservation.getReservationId());
//
//            stage.close();
//        }
//        else {
//            System.out.println("Please fill in all fields.");
//        }
//    }


    //method to open event window
    private void openRoomDialog(Integer reservationId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-room-page1"));
            Parent root = loader.load();

            // Get the controller of Event UI
            RoomController roomController = loader.getController();
            roomController.setReservationId(reservationId); // you need a setter in RoomController

            // Show Event form as modal
            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Add Event for Reservation ID: " + reservationId);
            modalStage.setScene(new Scene(root));
            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //method to open event window
    private void openAddEventDialog(Integer reservationId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/eventFinal.fxml"));

            // Tell FXMLLoader to get controllers from Spring
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean);

            Parent root = loader.load();

            EventUIController eventController = loader.getController();
            eventController.setReservationId(reservationId);

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Add Event for Reservation ID: " + reservationId);
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

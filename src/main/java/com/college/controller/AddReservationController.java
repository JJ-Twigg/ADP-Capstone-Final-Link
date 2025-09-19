package com.college.controller;

import com.college.domain.Reservation;
import com.college.service.ReservationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AddReservationController {

    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;

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

            openAddEventDialog(savedReservation.getReservationId());

            stage.close();
        }
        else {
            System.out.println("Please fill in all fields.");
        }
    }


    //method to open event window
    private void openAddEventDialog(Integer reservationId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-event.fxml"));
            Parent root = loader.load();

            // Get the controller of Event UI
            EventUIController eventController = loader.getController();
            eventController.setReservationId(reservationId); // pass the FK

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

    @FXML
    private void cancel() {
        stage.close();
    }
}

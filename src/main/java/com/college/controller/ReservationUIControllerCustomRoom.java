package com.college.controller;

import com.college.MainFinal;
import com.college.domain.CustomRoom;
import com.college.domain.Guest;
import com.college.domain.Reservation;
import com.college.service.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class ReservationUIControllerCustomRoom implements Initializable {

    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, Integer> reservationIdColumn;
    @FXML private TableColumn<Reservation, Integer> guestIdColumn;
    @FXML private TableColumn<Reservation, String> startTimeColumn;
    @FXML private TableColumn<Reservation, String> endTimeColumn;
    @FXML private TableColumn<Reservation, Integer> customRoomIdColumn;
    @FXML private TableColumn<Reservation, Integer> employeeIdColumn;

    @FXML private TableColumn<Reservation, Integer> numGuestsColumn;

    @FXML private TextField searchbar;
    @FXML private Label labelFeedback;

    private Stage stage;
    public void setStage(Stage stage) { this.stage = stage; }

    @Autowired private CustomRoomService customRoomService;
    @Autowired private GuestService guestService;
    @Autowired private PaymentService paymentService;
    @Autowired private EventUIServiceNaked eventService;

    @Autowired
    private NumGuestCache numGuestCache;

    @Autowired
    ReservationService reservationService;

    private ObservableList<Reservation> reservationList;
    private Guest guest;

    public void setGuest(Guest guest) { this.guest = guest; }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reservationIdColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getReservationId()).asObject());

        guestIdColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getGuest().getGuestId()).asObject());

        startTimeColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getReservationDateTimeStart()));

        endTimeColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getReservationDateTimeEnd()));

        customRoomIdColumn.setCellValueFactory(cellData -> {
            Reservation res = cellData.getValue();
            CustomRoom cr = customRoomService.getAll().stream()
                    .filter(room -> room.getReservation() != null && room.getReservation().getReservationId() == res.getReservationId())
                    .findFirst().orElse(null);
            return new SimpleIntegerProperty(cr != null ? cr.getRoomID() : 0).asObject();
        });

        employeeIdColumn.setCellValueFactory(cellData -> {
            Reservation res = cellData.getValue();
            CustomRoom cr = customRoomService.getAll().stream()
                    .filter(room -> room.getReservation() != null && room.getReservation().getReservationId() == res.getReservationId())
                    .findFirst().orElse(null);
            int empId = (cr != null && cr.getEmployee() != null) ? cr.getEmployee().getEmployeeId() : 0;
            return new SimpleIntegerProperty(empId).asObject();
        });

        reservationList = FXCollections.observableArrayList();
        reservationTable.setItems(reservationList);


        //NUM GUESTS
        numGuestsColumn.setCellValueFactory(cellData -> {
            Reservation r = cellData.getValue();
            // Get number of guests from cache, default to 0 if null
            Integer numGuests = numGuestCache.getNumGuests(r.getReservationId());
            if (numGuests == null) {
                numGuests = 0;
            }
            return new SimpleIntegerProperty(numGuests).asObject();
        });

        loadReservationData();
    }

    @FXML
    private void loadReservationData() {
        labelFeedback.setText("");
        List<CustomRoom> customRoomsWithReservations = customRoomService.getAll().stream()
                .filter(cr -> cr.getReservation() != null)
                .toList();

        reservationList.clear();
        for (CustomRoom cr : customRoomsWithReservations) {
            reservationList.add(cr.getReservation());
        }
        System.out.println("CustomRoom reservations loaded: " + reservationList.size());
    }

    @FXML
    private void search() {
        labelFeedback.setText("");
        String searchText = searchbar.getText().trim();

        if (searchText.isEmpty()) {
            labelFeedback.setText("Please enter an ID to search.");
            loadReservationData();
            return;
        }

        try {
            int id = Integer.parseInt(searchText);
            Reservation found = reservationList.stream()
                    .filter(r -> r.getReservationId() == id)
                    .findFirst().orElse(null);

            reservationList.clear();
            if (found != null) {
                reservationList.add(found);
                labelFeedback.setText("Record found for ID: " + id);
            } else {
                labelFeedback.setText("No record found for ID: " + id);
            }
        } catch (NumberFormatException e) {
            labelFeedback.setText("Invalid ID. Please enter a number.");
        }
    }

    @FXML
    private void delete() {
        Reservation selectedReservation = reservationTable.getSelectionModel().getSelectedItem();

        if (selectedReservation == null) {
            labelFeedback.setText("Please select a reservation to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Reservation?");
        alert.setContentText("Are you sure you want to delete reservation ID: " + selectedReservation.getReservationId() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                int guestId = selectedReservation.getGuest().getGuestId();

                // Delete linked event if exists
                if (selectedReservation.getEvent() != null) {
                    eventService.deleteByReservationId(selectedReservation.getReservationId());
                }

                // Nullify employee FK and reservation in CustomRoom
                CustomRoom cr = customRoomService.getAll().stream()
                        .filter(room -> room.getReservation() != null &&
                                room.getReservation().getReservationId() == selectedReservation.getReservationId())
                        .findFirst().orElse(null);

                if (cr != null) {
                    cr.setReservation(null);
                    cr.setEmployee(null);
                    cr.setAvailability(true);
                    customRoomService.update(cr);
                }

                // Delete reservation
                boolean deleted = reservationService.delete(selectedReservation.getReservationId());

                // Delete payment info if exists
                paymentService.deleteByGuestId(guestId);

                // Check if guest has any other reservations
                List<Reservation> remainingReservations = reservationService.getByGuestId(guestId);
                if (remainingReservations.isEmpty()) {
                    boolean guestDeleted = guestService.delete(guestId);
                    if (guestDeleted) {
                        System.out.println("Guest ID " + guestId + " deleted successfully.");
                    } else {
                        System.out.println("Failed to delete Guest ID " + guestId + ".");
                    }
                }

                // Feedback to user
                if (deleted) {
                    labelFeedback.setText("Reservation ID: " + selectedReservation.getReservationId() + " deleted successfully.");
                    loadReservationData();
                } else {
                    labelFeedback.setText("Failed to delete reservation ID: " + selectedReservation.getReservationId() + ".");
                }

            } catch (Exception e) {
                e.printStackTrace();
                labelFeedback.setText("Error deleting reservation: " + e.getMessage());
            }
        } else {
            labelFeedback.setText("Deletion cancelled.");
        }
    }


    @FXML
    private void update() {
        Reservation selectedReservation = reservationTable.getSelectionModel().getSelectedItem();

        if (selectedReservation == null) {
            labelFeedback.setText("Please select a reservation to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dialog_boxes/edit-reservation.fxml"));
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean);

            Parent root = loader.load();
            EditReservationController editController = loader.getController();

            // Pass reservation to edit
            editController.setReservationToEdit(selectedReservation);

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Edit CustomRoom Reservation (ID: " + selectedReservation.getReservationId() + ")");
            modalStage.setScene(new Scene(root));
            editController.setStage(modalStage);

            modalStage.showAndWait();
            loadReservationData();

        } catch (IOException e) {
            e.printStackTrace();
            labelFeedback.setText("Error opening Edit Reservation form.");
        }
    }

}

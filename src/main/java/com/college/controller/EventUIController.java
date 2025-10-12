package com.college.controller;

import com.college.MainFinal;
import com.college.domain.Event;
import com.college.domain.Reservation;
import com.college.service.EventUIService;
import com.college.service.EventUIServiceNaked;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EventUIController {

    @FXML private TextField reasonField;
    @FXML private TextField descriptionField;
    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, Integer> colId;
    @FXML
    private TableColumn<Event, Integer> colReservationId;

    @FXML private TableColumn<Event, String> colReason;
    @FXML private TableColumn<Event, String> colDescription;

    @FXML
    private HBox reasonBox;       // wrap reasonField in HBox in FXML and fx:id="reasonBox"
    @FXML
    private HBox descriptionBox;

    @FXML
    private Button addButton;

    private final EventUIServiceNaked eventService;

    // ✅ Constructor injection
    public EventUIController(EventUIServiceNaked eventService) {
        this.eventService = eventService;
    }

    private final ObservableList<Event> eventList = FXCollections.observableArrayList();

    // FK for Event
    private Reservation reservation;

    // FK for Event setter
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }


    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getEventId()).asObject());

        colId.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        colReservationId.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getReservation().getReservationId()).asObject()
        );

        colReason.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getReason()));
        colDescription.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));

        loadEvents();
    }

    private void loadEvents() {
        try {
            eventList.setAll(eventService.getAllEvents());
            eventTable.setItems(eventList);
        } catch (Exception e) {
            showAlert("Error", "Failed to load events: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddEvent() {
        String reason = reasonField.getText().trim();
        String description = descriptionField.getText().trim();

        if (reason.isEmpty()) {
            showAlert("Validation Error", "Reason cannot be empty.");
            return;
        }

        if (description.isEmpty()) {
            showAlert("Validation Error", "Description cannot be empty.");
            return;
        }


        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Save");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to save this event?");
        if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return; // User cancelled, do not save
        }


        Event event = new Event();
        event.setReason(reason);
        event.setDescription(description);

        // SET FK HERE
        event.setReservation(reservation);

        try {
            eventService.addEvent(event);
            reasonField.clear();
            descriptionField.clear();
            loadEvents();

            addButton.setDisable(true);

            Stage currentStage = (Stage) addButton.getScene().getWindow();

            currentStage.close();

            openPaymentPage();

        } catch (Exception e) {
            showAlert("Error", "Failed to add event: " + e.getMessage());
        }
    }




    @FXML
    private void handleEditEvent() {
        Event selected = eventTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "Please select an event to edit.");
            return;
        }

        String newReason = reasonField.getText().trim();
        String newDescription = descriptionField.getText().trim();

        try {
            // Fetch managed entity
            Event managedEvent = eventService.getEvent(selected.getEventId());
            if (managedEvent == null) {
                showAlert("Error", "Event not found in database.");
                return;
            }

            // Update fields
            if (!newReason.isEmpty()) managedEvent.setReason(newReason);
            if (!newDescription.isEmpty()) managedEvent.setDescription(newDescription);

            // Save updated managed entity
            eventService.updateEvent(managedEvent);

            reasonField.clear();
            descriptionField.clear();
            loadEvents();

            showAlert("Success", "Event updated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update event: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteEvent() {
        Event selected = eventTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "Please select an event to delete.");
            return;
        }

        System.out.println("Attempting to delete event ID: " + selected.getEventId());

        try {
            // First, fetch the managed entity from the repository
            Event eventToDelete = eventService.getEvent(selected.getEventId());
            if (eventToDelete == null) {
                System.out.println("Event not found in DB");
                showAlert("Error", "Event not found in database.");
                return;
            }

            // Delete using managed entity
            eventService.deleteEvent(eventToDelete.getEventId());

            System.out.println("Deleted successful");

            // Refresh table
            loadEvents();

        } catch (Exception e) {
            e.printStackTrace(); // <-- will show actual DB/JPA error
            showAlert("Error", "Failed to delete event: " + e.getMessage());
        }
    }



    private void openPaymentPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/paymentFinal.fxml"));
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean);
            Parent root = loader.load();

            PaymentViewController paymentController = loader.getController();
            if (reservation != null && reservation.getGuest() != null) {
                paymentController.setGuest(reservation.getGuest());
            } else {
                showAlert("Error", "No guest FK found for this reservation!");
                return;
            }

            Stage stage = new Stage();
            stage.setTitle("Payment");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Payment window: " + e.getMessage());
        }
    }







    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showAddEventInputs(ActionEvent actionEvent) {
        // Make the input fields visible
        reasonBox.setVisible(true);
        descriptionBox.setVisible(true);

        // Optional: move focus to the first field
        reasonField.requestFocus();
    }
}
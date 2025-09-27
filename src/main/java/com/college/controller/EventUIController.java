package com.college.controller;

import com.college.domain.Event;
import com.college.domain.Reservation;
import com.college.service.EventUIService;
import com.college.service.EventUIServiceNaked;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

@Component
public class EventUIController {

    @FXML private TextField reasonField;
    @FXML private TextField descriptionField;
    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, Integer> colId;
    @FXML private TableColumn<Event, String> colReason;
    @FXML private TableColumn<Event, String> colDescription;

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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
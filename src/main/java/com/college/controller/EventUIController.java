package com.college.controller;

import com.college.domain.Event;
import com.college.service.EventUIService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EventUIController {

    @FXML private TextField reasonField;
    @FXML private TextField descriptionField;
    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, Integer> colId;
    @FXML private TableColumn<Event, String> colReason;
    @FXML private TableColumn<Event, String> colDescription;

    private final EventUIService eventService = new EventUIService();
    private final ObservableList<Event> eventList = FXCollections.observableArrayList();

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

        try {
            eventService.addEvent(event);
            reasonField.clear();
            descriptionField.clear();
            loadEvents();
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

        // Keep the ID so backend knows which record to update
        Event updatedEvent = new Event();
        updatedEvent.setEventId(selected.getEventId());
        updatedEvent.setReason(!newReason.isEmpty() ? newReason : selected.getReason());
        updatedEvent.setDescription(!newDescription.isEmpty() ? newDescription : selected.getDescription());

        try {
            eventService.updateEvent(updatedEvent);
            reasonField.clear();
            descriptionField.clear();
            loadEvents();
        } catch (Exception e) {
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

        try {
            eventService.deleteEvent(selected.getEventId());
            loadEvents();
        } catch (Exception e) {
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
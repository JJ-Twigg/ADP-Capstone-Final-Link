package com.college.controller;

import com.college.domain.Event;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class AddEditEventDialog extends Dialog<Event> {

    private final TextField reasonField = new TextField();
    private final TextArea descriptionArea = new TextArea();

    public AddEditEventDialog(Event event) {
        setTitle(event == null ? "Add Event" : "Edit Event");
        setHeaderText(event == null ? "Enter new event details" : "Update event details");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        reasonField.setPromptText("Reason (Holiday, Work, etc.)");
        descriptionArea.setPromptText("Enter description...");
        descriptionArea.setPrefRowCount(4);

        grid.add(new Label("Reason:"), 0, 0);
        grid.add(reasonField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionArea, 1, 1);

        getDialogPane().setContent(grid);

        // Fill fields if editing
        if (event != null) {
            reasonField.setText(event.getReason());
            descriptionArea.setText(event.getDescription());
        }

        Node saveButton = getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true);

        // Validation: enable save only if reason is filled
        reasonField.textProperty().addListener((obs, oldVal, newVal) -> {
            saveButton.setDisable(newVal.trim().isEmpty());
        });

        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Event(
                        event != null ? event.getEventId() : 0,
                        reasonField.getText(),
                        descriptionArea.getText()
                );
            }
            return null;
        });
    }
}
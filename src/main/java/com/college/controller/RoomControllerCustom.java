package com.college.controller;

import com.college.domain.CustomRoom;
import com.college.service.CustomRoomService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomControllerCustom {

    @Autowired
    private CustomRoomService customRoomService;

    @FXML
    private VBox roomContainer;

    private static final int CARDS_PER_ROW = 3;

    @FXML
    private void initialize() {
        loadRoomsFromDB();
    }

    private void loadRoomsFromDB() {
        List<CustomRoom> allRooms = customRoomService.getAll()
                .stream()
                .filter(r -> r.getRoomID() >= 60 && r.getRoomID() <= 70)
                .sorted(Comparator.comparingInt(CustomRoom::getRoomID))
                .collect(Collectors.toList());

        roomContainer.getChildren().clear();

        if (allRooms.isEmpty()) {
            Label noRooms = new Label("No rooms available in the range 60–70.");
            noRooms.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");
            roomContainer.getChildren().add(noRooms);
            return;
        }

        String[] imageNames = {"a", "b", "c", "d", "e", "f"};

        HBox row = new HBox(20);       // spacing between cards
        row.setAlignment(Pos.CENTER);   // center cards in row
        int count = 0;

        for (CustomRoom room : allRooms) {
            String imageName = imageNames[room.getRoomID() % imageNames.length];
            VBox card = createRoomCard(room);

            // --- CARD FIXES FOR GRID-LIKE LAYOUT ---
            card.setPrefWidth(220);
            card.setMaxWidth(220);
            card.setMinWidth(220);
            card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-spacing: 10; -fx-alignment: top_center; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 2, 2);");
            // ------------------------------

            row.getChildren().add(card);
            count++;

            if (count % CARDS_PER_ROW == 0) {
                roomContainer.getChildren().add(row);
                row = new HBox(20);
                row.setAlignment(Pos.CENTER);
            }
        }

        if (!row.getChildren().isEmpty()) {
            roomContainer.getChildren().add(row);
        }
    }


    private VBox createRoomCard(CustomRoom customRoom) {
        VBox card = new VBox(10);
        card.getStyleClass().add("room-card");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(220);
        card.setMaxWidth(220);
        card.setMinWidth(220);

        ImageView roomImage = new ImageView();
        roomImage.setFitWidth(200);
        roomImage.setFitHeight(150);
        roomImage.setPreserveRatio(false);

        if (customRoom.getImage() != null && customRoom.getImage().length > 0) {
            roomImage.setImage(new Image(new ByteArrayInputStream(customRoom.getImage())));
        } else {
            URL placeholderUrl = getClass().getClassLoader().getResource("images/placeholder.jpg");
            if (placeholderUrl != null) {
                roomImage.setImage(new Image(placeholderUrl.toExternalForm()));
            }
        }

        card.getChildren().add(roomImage);

        Label idLabel = new Label("Room ID: " + customRoom.getRoomID());
        idLabel.getStyleClass().add("room-title");
        card.getChildren().add(idLabel);

        card.getChildren().add(createEditableField("Price Per Night:", String.valueOf(customRoom.getPricePerNight()), val -> customRoom.setPricePerNight(Float.parseFloat(val))));
        card.getChildren().add(createEditableField("Room Type:", customRoom.getRoomType(), customRoom::setRoomType));
        card.getChildren().add(createNonEditableField("Availability:", customRoom.getAvailability() ? "Yes" : "No"));
        card.getChildren().add(createEditableField("Features:", customRoom.getFeatures(), customRoom::setFeatures));

        // --- UPDATE BUTTON ---
        Button updateBtn = new Button("Update Room");
        updateBtn.setMaxWidth(Double.MAX_VALUE);
        updateBtn.setStyle(
                "-fx-background-color: #398F6C;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 16;" +
                        "-fx-background-radius: 3;" +
                        "-fx-cursor: hand;"
        );
        updateBtn.setOnMouseEntered(e -> updateBtn.setStyle(
                "-fx-background-color: #ADD8E6;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 16;" +
                        "-fx-background-radius: 3;" +
                        "-fx-cursor: hand;"
        ));
        updateBtn.setOnMouseExited(e -> updateBtn.setStyle(
                "-fx-background-color: #398F6C;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 16;" +
                        "-fx-background-radius: 3;" +
                        "-fx-cursor: hand;"
        ));
        updateBtn.setOnAction(event -> {
            try {
                customRoomService.update(customRoom);
                System.out.println("CustomRoom " + customRoom.getRoomID() + " updated successfully.");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Room Updated");
                alert.setHeaderText(null);
                alert.setContentText("Room " + customRoom.getRoomID() + " updated successfully.");
                alert.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // --- DELETE BUTTON ---
        Button deleteBtn = new Button("Delete Room");
        deleteBtn.setMaxWidth(Double.MAX_VALUE);
        deleteBtn.setStyle(
                "-fx-background-color: #D9534F;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 16;" +
                        "-fx-background-radius: 3;" +
                        "-fx-cursor: hand;"
        );
        deleteBtn.setOnMouseEntered(e -> deleteBtn.setStyle(
                "-fx-background-color: #FF6F61;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 16;" +
                        "-fx-background-radius: 3;" +
                        "-fx-cursor: hand;"
        ));
        deleteBtn.setOnMouseExited(e -> deleteBtn.setStyle(
                "-fx-background-color: #D9534F;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 16;" +
                        "-fx-background-radius: 3;" +
                        "-fx-cursor: hand;"
        ));
        deleteBtn.setOnAction(event -> {
            try {
                // Prevent deletion if room is unavailable
                if (!customRoom.getAvailability()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Cannot Delete Room");
                    alert.setHeaderText(null);
                    alert.setContentText("Room " + customRoom.getRoomID() + " is Booked and cannot be deleted.");
                    alert.showAndWait();
                    return; // stop deletion
                }

                // Existing delete logic
                customRoomService.delete(customRoom.getRoomID());
                roomContainer.getChildren().clear();
                loadRoomsFromDB();
                System.out.println("CustomRoom " + customRoom.getRoomID() + " deleted successfully.");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Room Deleted");
                alert.setHeaderText(null);
                alert.setContentText("Room " + customRoom.getRoomID() + " deleted successfully.");
                alert.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Add buttons to card
        card.getChildren().addAll(updateBtn, deleteBtn);

        return card;
    }



    private HBox createEditableField(String labelPrefix, String value, java.util.function.Consumer<String> setter) {
        HBox hbox = new HBox(5);

        Label label = new Label(labelPrefix + " " + value);
        TextField textField = new TextField(value);
        textField.setVisible(false);
        textField.setManaged(false);

        URL iconUrl = getClass().getClassLoader().getResource("images/icons/green.png");
        ImageView editIcon = new ImageView(iconUrl != null ? new Image(iconUrl.toExternalForm()) : null);
        editIcon.setFitWidth(16);
        editIcon.setFitHeight(16);
        editIcon.setPreserveRatio(true);

        editIcon.setOnMouseClicked(event -> {
            label.setVisible(false);
            label.setManaged(false);
            textField.setVisible(true);
            textField.setManaged(true);
            textField.requestFocus();
        });

        textField.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                String newValue = textField.getText().trim();
                setter.accept(newValue);
                label.setText(labelPrefix + " " + newValue);
                label.setVisible(true);
                label.setManaged(true);
                textField.setVisible(false);
                textField.setManaged(false);
            }
        });

        hbox.getChildren().addAll(label, textField, editIcon);
        return hbox;
    }

    private HBox createNonEditableField(String labelPrefix, String value) {
        HBox hbox = new HBox(5);
        Label label = new Label(labelPrefix + " " + value);
        hbox.getChildren().add(label);
        return hbox;
    }


    public void setCustomRoomService(CustomRoomService customRoomService) {
        this.customRoomService = customRoomService;
    }
}

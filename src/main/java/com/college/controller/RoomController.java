package com.college.controller;

import com.college.domain.Room;
import com.college.service.RoomService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomController {

    @Autowired
    private RoomService roomService;

    @FXML
    private VBox roomContainer;

    private static final int CARDS_PER_ROW = 3;

    @FXML
    private void initialize() {
        loadRoomsFromDB(0);
    }


    private int currentPage = 0;

    private static final int ROWS_PER_PAGE = 1;   // rows per page
    private static final int ROOMS_PER_PAGE = CARDS_PER_ROW * ROWS_PER_PAGE;

    private void loadRoomsFromDB(int page) {
        List<Room> allRooms = roomService.getAll()
                .stream()
                .sorted(Comparator.comparingInt(Room::getRoomID))
                .collect(Collectors.toList());

        roomContainer.getChildren().clear(); // clear previous cards
        String[] imageNames = {"a", "b", "c", "d", "e", "f"};

        List<Room> roomsOnPage;

        if (page == 0) {
            // Page 1: fixed IDs 51–56
            roomsOnPage = allRooms.stream()
                    .filter(r -> r.getRoomID() >= 51 && r.getRoomID() <= 56)
                    .collect(Collectors.toList());
        } else if (page == 1) {
            // Page 2: all rooms with ID > 56
            roomsOnPage = allRooms.stream()
                    .filter(r -> r.getRoomID() > 56)
                    .collect(Collectors.toList());
            if (roomsOnPage.isEmpty()) return; // no more rooms
        } else {
            return; // no further pages
        }

        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER);
        int count = 0;

        for (Room room : roomsOnPage) {
            String imageName = imageNames[room.getRoomID() % imageNames.length];
            VBox card = createRoomCard(room, imageName);
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

        currentPage = page;
    }




    private VBox createRoomCard(Room room, String imageName) {
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

        // Load image by fixed name
        URL roomUrl = getClass().getClassLoader().getResource("images/rooms/" + imageName + ".jpg");
        if (roomUrl != null) {
            roomImage.setImage(new Image(roomUrl.toExternalForm()));
        } else {
            URL placeholderUrl = getClass().getClassLoader().getResource("images/placeholder.jpg");
            if (placeholderUrl != null) {
                roomImage.setImage(new Image(placeholderUrl.toExternalForm()));
            } else {
                System.err.println("Missing placeholder image for room " + room.getRoomID());
            }
        }

        card.getChildren().add(roomImage);

        Label idLabel = new Label("Room ID: " + room.getRoomID());
        idLabel.getStyleClass().add("room-title");
        card.getChildren().add(idLabel);

        // Editable fields
        card.getChildren().add(createEditableField("Price Per Night:", String.valueOf(room.getPricePerNight()), val -> room.setPricePerNight(Float.parseFloat(val))));
        card.getChildren().add(createEditableField("Room Type:", room.getRoomType(), room::setRoomType));
        card.getChildren().add(createNonEditableField("Availability:", room.getAvailability() ? "Yes" : "No"));
        card.getChildren().add(createEditableField("Features:", room.getFeatures(), room::setFeatures));

        Button updateBtn = new Button("Update Room");
        updateBtn.setMaxWidth(Double.MAX_VALUE);
        updateBtn.setOnAction(event -> roomService.update(room));
        updateBtn.getStyleClass().add("room-button");
        card.getChildren().add(updateBtn);

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

    @FXML
    private void nextPage(ActionEvent event) {
        if (currentPage < 1) { // only 2 pages: 0 and 1
            currentPage++;
            loadRoomsFromDB(currentPage);
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        if (currentPage > 0) {
            currentPage--;
            loadRoomsFromDB(currentPage);
        }
    }

    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }
}

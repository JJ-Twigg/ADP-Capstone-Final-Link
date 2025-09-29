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
        loadRoomsFromDB();
    }

    private void loadRoomsFromDB() {
        // Only load rooms with IDs 51–56
        List<Room> rooms = roomService.getAll()
                .stream()
                .filter(r -> r.getRoomID() >= 51 && r.getRoomID() <= 56)
                .collect(Collectors.toList());

        String[] imageNames = {"a", "b", "c", "d", "e", "f"}; // fixed images

        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER);
        int count = 0;

        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            String imageName = imageNames[i % imageNames.length];
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
    }

    private VBox createRoomCard(Room room, String imageName) {
        VBox card = new VBox(10);
        card.getStyleClass().add("room-card");

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

        card.getChildren().add(createEditableField("Price Per Night:", String.valueOf(room.getPricePerNight()), val -> room.setPricePerNight(Float.parseFloat(val))));
        card.getChildren().add(createEditableField("Room Type:", room.getRoomType(), room::setRoomType));
        card.getChildren().add(createEditableField("Availability:", room.getAvailability() ? "Yes" : "No", val -> room.setAvailability(val.equalsIgnoreCase("Yes"))));
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

    @FXML
    private void nextPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-room-page2.fxml"));
            Parent page2 = loader.load();
            RoomController controller = loader.getController();
            controller.setRoomService(this.roomService);
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(page2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-room-page1.fxml"));
            Parent page1 = loader.load();
            RoomController controller = loader.getController();
            controller.setRoomService(this.roomService);
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(page1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }
}

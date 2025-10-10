package com.college.controller;

import com.college.domain.CustomRoom;
import com.college.domain.Room;
import com.college.factory.CustomRoomFactory;
import com.college.factory.RoomFactory;
import com.college.service.CustomRoomService;
import com.college.service.RoomService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.util.Optional;

@Component
public class AdminAddRoomController {

    @FXML private ComboBox<String> roomIdComboBox;
    @FXML private TextField roomTypeField;
    @FXML private TextField pricePerNightField;
    @FXML private ComboBox<String> availabilityComboBox;
    @FXML private TextField featuresField;

    @FXML
    private ImageView roomImageView;

//    @Autowired
//    RoomService roomService;

    @Autowired
    CustomRoomService customRoomService;

    private File selectedImageFile;


    @FXML
    public void handleUploadImage(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Room Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );

            File file = fileChooser.showOpenDialog(null);

            if (file != null) {
                selectedImageFile = file;
                Image image = new Image(file.toURI().toString());
                roomImageView.setImage(image);
            } else {
                showAlert("No file selected", "Please choose an image file.");
            }
        } catch (Exception e) {
            showAlert("Error", "Unable to upload image: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void addRoomToDatabase() {
        try {
            int roomID = Integer.parseInt(roomIdComboBox.getValue());
            String roomType = roomTypeField.getText();
            float pricePerNight = Float.parseFloat(pricePerNightField.getText());
            boolean availability = "Available".equalsIgnoreCase(availabilityComboBox.getValue());
            String features = featuresField.getText();

            byte[] imageBytes = null;
            if (selectedImageFile != null) {
                imageBytes = Files.readAllBytes(selectedImageFile.toPath());
            }

            // Show confirmation dialog before saving
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Save");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Are you sure you want to save this room?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isEmpty() || result.get() != javafx.scene.control.ButtonType.OK) {
                // User cancelled
                System.out.println("Room save cancelled.");
                return;
            }

            // Create room object
            CustomRoom customRoom = CustomRoomFactory.createCustomRoom(
                    roomID, roomType, pricePerNight, availability, features, imageBytes
            );

            // Save to database
            customRoomService.create(customRoom);

            showAlert("Success", "Room added successfully!");

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numeric values for Room ID and Price.");
        } catch (Exception e) {
            showAlert("Error", "Failed to add room: " + e.getMessage());
            e.printStackTrace();
        }
    }



    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}

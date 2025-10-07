package com.college.controller;

import com.college.domain.Room;
import com.college.factory.RoomFactory;
import com.college.service.RoomService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class AdminAddRoomController {

    @FXML private ComboBox<String> roomIdComboBox;
    @FXML private TextField roomTypeField;
    @FXML private TextField pricePerNightField;
    @FXML private ComboBox<String> availabilityComboBox;
    @FXML private TextField featuresField;

    @FXML
    private ImageView roomImageView;

    @Autowired
    RoomService roomService;

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


    public void addRoomToDatabase(){
        try {
            // Retrieve values from fields
            int roomID = Integer.parseInt(roomIdComboBox.getValue());
            String roomType = roomTypeField.getText();
            float pricePerNight = Float.parseFloat(pricePerNightField.getText());
            boolean availability = "Available".equalsIgnoreCase(availabilityComboBox.getValue());
            String features = featuresField.getText();

            Room customRoom = RoomFactory.createRoom(roomID,roomType,pricePerNight,availability,features);

            roomService.create(customRoom);






        } catch (NumberFormatException e) {

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

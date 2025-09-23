package com.college.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;
import java.util.Optional;

@Controller
public class DashboardController {

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        // Just initialize, don't load any views automatically
        System.out.println("DashboardController initialized");

        // Show a simple welcome message
        contentArea.getChildren().clear();
        Label welcome = new Label("Welcome! Click a menu option to start.");
        welcome.setStyle("-fx-font-size: 18px; -fx-text-fill: #2c3e50;");
        contentArea.getChildren().add(welcome);
    }




    @FXML
    public void showOverview() {
        safeLoadView("/scenes/overview1.fxml", "Overview");
    }



    @FXML
    public void showReservation() {
        safeLoadView("/scenes/window-reservation.fxml", "Reservation");
    }



    @FXML
    public void showGuests() {
        safeLoadView("/scenes/guest-view.fxml", "Guests");
    }


    @FXML
    public void showPayments() {
        safeLoadView("/scenes/payment-view.fxml", "Payments");
    }

    @FXML
    public void showEmployees() {
        safeLoadView("/scenes/employee-view.fxml", "Employees");
    }

    @FXML
    public void showRooms() {
        safeLoadView("/scenes/room-view.fxml", "Rooms");
    }

    @FXML
    public void showVenues() {
        safeLoadView("/scenes/venue-view.fxml", "Venues");
    }





    private void safeLoadView(String fxmlPath, String viewName) {
        try {
            if (getClass().getResource(fxmlPath) == null) {
                throw new Exception("File not found: " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            System.out.println("Error loading " + viewName + " view: " + e.getMessage());

            contentArea.getChildren().clear();
            Label messageLabel = new Label(viewName + " view is under construction");
            messageLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #2c3e50;");
            contentArea.getChildren().add(messageLabel);
        }
    }





    @FXML
    public void exitApplication() {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit Application");
            alert.setHeaderText("Are you sure you want to exit?");
            alert.setContentText("Any unsaved changes will be lost.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Stage stage = (Stage) contentArea.getScene().getWindow();
                stage.close();
            }
        });
    }
}

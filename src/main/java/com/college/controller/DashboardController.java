package com.college.controller;

import com.college.MainFinal;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
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
    public void showGuests() {
        safeLoadViewOtherPages("/scenes/guestFinal.fxml", "Guests");
    }

    @FXML
    public void showGuestSolo() {
        safeLoadViewOtherPages("/scenes/guestFinalAll.fxml", "Reservation");
    }

    @FXML
    public void showReservationSolo() {
        safeLoadViewOtherPages("/scenes/reservationFinalAll.fxml", "Reservation");
    }

    @FXML
    public void showEventsSolo() {
        safeLoadViewOtherPages("/scenes/eventFinalAll.fxml", "Reservation");
    }

    @FXML
    public void showPaymentsSolo() {
        safeLoadViewOtherPages("/scenes/paymentFinalAll.fxml", "Reservation");
    }

    @FXML
    public void showRooms() {
        safeLoadViewOtherPages("/scenes/window-room-page1.fxml", "Rooms");
    }





    @FXML
    public void showPayments() {
        safeLoadViewOtherPages("/scenes/paymentFinal.fxml", "Payments");
    }

    @FXML
    public void showEmployees() {
        safeLoadViewOtherPages("/scenes/employeeFinal.fxml", "Employees");
    }


    @FXML
    public void showShifts() {
        safeLoadViewOtherPages("/scenes/shiftFinal.fxml", "Shifts");
    }

    @FXML
    public void showShiftsUser() {
        safeLoadViewOtherPages("/scenes/shiftFinalUser.fxml", "Shifts");
    }

    @FXML
    public void showEmployeeSalary() {
        safeLoadViewOtherPages("/scenes/EmployeeSalary.fxml", "Employee Salaries");
    }

    @FXML
    public void showUsers() {
        safeLoadViewOtherPages("/scenes/UsersFinal.fxml", "Users");
    }






    private void safeLoadView(String fxmlPath, String viewName) {
        try {
            if (getClass().getResource(fxmlPath) == null) {
                throw new Exception("File not found: " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            //added this new for card data
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean);

            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            System.out.println("Error loading " + viewName + " view: " + e.getMessage());
            e.printStackTrace();

            contentArea.getChildren().clear();
            Label messageLabel = new Label(viewName + " view is under construction");
            messageLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #2c3e50;");
            contentArea.getChildren().add(messageLabel);
        }
    }


    private void safeLoadViewOtherPages(String fxmlPath, String viewName) {
        try {
            if (getClass().getResource(fxmlPath) == null) {
                throw new Exception("File not found: " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean); // <-- Spring injection
            Parent view = loader.load();

            if ("/scenes/window-room-page1.fxml".equals(fxmlPath)) {
                view.getStylesheets().add(getClass().getResource("/css/buttonStyle.css").toExternalForm());
            }else if("/scenes/window-room-page2.fxml".equals(fxmlPath)){
                view.getStylesheets().add(getClass().getResource("/css/buttonStyle.css").toExternalForm());
            }


            contentArea.getChildren().setAll(view);

        } catch (Exception e) {
            System.out.println("Error loading " + viewName + " view: " + e.getMessage());
            e.printStackTrace();
            contentArea.getChildren().clear();
            Label messageLabel = new Label(viewName + " view is under construction");
            messageLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #2c3e50;");
            contentArea.getChildren().add(messageLabel);
        }
    }

    private void openInNewWindow(String fxmlPath, String viewName) {
        try {
            if (getClass().getResource(fxmlPath) == null) {
                throw new Exception("File not found: " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean); // Spring injection
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(viewName);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // blocks other windows until closed (optional)
            stage.show();

        } catch (Exception e) {
            System.out.println("Error opening " + viewName + " window: " + e.getMessage());
            e.printStackTrace();

            // fallback: show a minimal popup
            Stage errorStage = new Stage();
            VBox box = new VBox(new Label(viewName + " window could not be loaded"));
            box.setStyle("-fx-padding: 20; -fx-font-size: 16;");
            errorStage.setScene(new Scene(box));
            errorStage.show();
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

package com.college.controller;

import com.college.MainFinal;
import com.college.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@Controller
public class DashboardController {

    @FXML
    private StackPane contentArea;


    @FXML
    private Label emailLabel;

    @FXML
    private Label roleLabel;

    String name;

    @FXML
    private ImageView profileImage;

    @Autowired
    private UserService userService;




    // DashboardController
    private String currentEmail;

    // DashboardController
    private String currentName;


    // When user edits email
    public void updateEmail(String newEmail) {
        currentEmail = newEmail;
    }

    public void updateName(String newName) {
        currentName = newName;
    }







    public void setProfileImage(byte[] imageBytes) {
        if (imageBytes != null && imageBytes.length > 0) {
            Image img = new Image(new ByteArrayInputStream(imageBytes));
            profileImage.setImage(img);
        } else {
            // fallback to default image
            profileImage.setImage(new Image(getClass().getResource("/images/characters/download4.jpg").toExternalForm()));
        }
    }

    //Ammars set user info
    public void setUserInfo(String email) {
        emailLabel.setText(email);
    }

    public void setUserInfo(String email, String role) {
        emailLabel.setText(email);
        roleLabel.setText(role);
        byte[] userImage = userService.getUserPhoto(emailLabel.getText());
        setProfileImage(userImage);
    }

    public void setName(String name) {
        this.name = name;
    }

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
        safeLoadViewOtherPages("/scenes/guestFinalViewOnly.fxml", "Guests");
    }

    @FXML
    public void showGuestsGreen() {
        safeLoadViewOtherPages("/scenes/guestFinalGreen.fxml", "Guests");
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
    public void showReservationViewOnly() {
        safeLoadViewOtherPages("/scenes/reservationFinalAllViewOnly.fxml", "Reservation");
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
    public void showPrint() {
        safeLoadViewOtherPages("/scenes/printFinal.fxml", "Print");
    }

    @FXML
    public void showSearchGuest() {
        safeLoadViewOtherPages("/scenes/guestSearchFinal.fxml", "Search For A Guest");
    }

    @FXML
    public void showSearchTopPayment() {safeLoadViewOtherPages("/scenes/paymentSearchFinal.fxml", "Search Top Payments");
    }

    @FXML
    public void showRestful() {
        safeLoadView("/scenes/restFinal.fxml", "Restful Web Service");
    }

    @FXML
    public void showProfileManager2() {
        showProfileManager();
    }

    @FXML
    public void showFAQ() {
        safeLoadView("/scenes/FAQ.fxml", "FAQ");
    }

    @FXML
    public void showAdminAddRoom() {safeLoadView("/scenes/addRoomFinal.fxml", "Add Rooms");}

    @FXML
    public void showCustomRooms() {safeLoadView("/scenes/customRoomFinal.fxml", "Custom Rooms");}

//this is the old show-overview-manager before adding profile image persistence, just go back to this if the new method show overview manager
// if it breaks spring context or something for manager dash after doing this
//    @FXML
//    public void showOverviewManager() {
//        safeLoadView("/scenes/overviewManager.fxml", "Overview");
//    }




    @FXML
    public void showOverviewUser() {
        try {
            String fxmlPath = "/scenes/overviewUser.fxml";
            String viewName = "Overview";

            if (getClass().getResource(fxmlPath) == null) {
                throw new Exception("File not found: " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean);
            Parent view = loader.load();

            //  use the values already in DashboardController, email and role are given to dashboard when a user logs in
            String email = emailLabel.getText();
            String role = roleLabel.getText();

            //give to overviewController
            OverviewController controller = loader.getController();
            controller.setUserEmail(email);
            controller.setUserRole(role);
            controller.setName(name);

            controller.setDashboardController(this);
            controller.setUserEmail(currentEmail != null ? currentEmail : emailLabel.getText());
            controller.setName(currentName != null ? currentName : name);

            contentArea.getChildren().setAll(view);

        } catch (Exception e) {
            System.out.println("Error loading "  + " view: " + e.getMessage());
            e.printStackTrace();

            contentArea.getChildren().clear();
            Label messageLabel = new Label( " view is under construction");
            messageLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #2c3e50;");
            contentArea.getChildren().add(messageLabel);
        }
    }

    @FXML
    public void showOverviewManager() {
        try {
            String fxmlPath = "/scenes/overviewManager.fxml";
            String viewName = "Overview";

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean);
            Parent view = loader.load();

            // Pass the current logged-in user's email and role to the OverviewController
            String email = emailLabel.getText();
            String role = roleLabel.getText();

            OverviewController controller = loader.getController();

            controller.setUserEmail(email);
            controller.setUserRole(role);

            controller.setDashboardController(this); // for callbacks
            controller.setName(name);

            controller.setUserEmail(currentEmail != null ? currentEmail : email);
            controller.setName(currentName != null ? currentName : name);




            // **Update the dashboard image here, not in OverviewController**
            byte[] userImage = userService.getUserPhoto(email);
            setProfileImage(userImage); // DashboardController method

            contentArea.getChildren().setAll(view);

        } catch (Exception e) {
            System.out.println("Error loading " + " view: " + e.getMessage());
            e.printStackTrace();
        }
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
    public void showEmployeesGreen() {
        safeLoadViewOtherPages("/scenes/employeeFinalGreen.fxml", "Employees");
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

    @FXML
    public void showSignIn(ActionEvent event) {

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Navigation");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to sign out?");

        // Only proceed if user clicks OK
        if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            showSignInPage(event);
        } else {
            // User clicked Cancel, do nothing
            System.out.println("Navigation to login canceled by user.");
        }

//        safeLoadViewOtherPages("/scenes/window-login.fxml", "Login");
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
            }else if ("/scenes/customRoomFinal.fxml".equals(fxmlPath)) {
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
    public void showSignInPage(ActionEvent event) {
        try {
            // Load login FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-login.fxml"));
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean);
            Parent loginRoot = loader.load();

            // Get the current stage from the event
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setWidth(600);
            stage.setHeight(430);
            stage.centerOnScreen();
            stage.setResizable(false);

            // Set new scene
            Scene loginScene = new Scene(loginRoot);
            loginScene.getStylesheets().add(getClass().getResource("/css/buttonStyle.css").toExternalForm());

            stage.setScene(loginScene);
            stage.setTitle("Login");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showProfileManager() {
        try {
            String fxmlPath = "/scenes/profileManagerFinal.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean); // Spring injection
            Parent view = loader.load();

            // Pass email and name to ProfileController
            ProfileController controller = loader.getController();
            controller.setUserEmail(emailLabel.getText());


            contentArea.getChildren().setAll(view);

        } catch (Exception e) {
            System.out.println("Error loading Profile view: " + e.getMessage());
            e.printStackTrace();
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

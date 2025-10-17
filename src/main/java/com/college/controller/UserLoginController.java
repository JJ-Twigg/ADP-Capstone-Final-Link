package com.college.controller;

import com.college.MainFinal;
import com.college.config.DashboardAuthoriseHandler;
import com.college.domain.User;
import com.college.service.UserService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserLoginController {

    @FXML
    private Button loginButton;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @Autowired
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @FXML
    private StackPane mainFrame;

    @FXML
    private VBox leftPane;

    @FXML
    private HBox rootHBox;

    @FXML
    public void initialize() {
        // Bind left panel width to 40% of the HBox width
        leftPane.prefWidthProperty().bind(rootHBox.widthProperty().multiply(0.4));

        // Prevent maximize AFTER scene is attached
        Platform.runLater(() -> {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.maximizedProperty().addListener((obs, wasMaximized, isNowMaximized) -> {
                if (isNowMaximized) {
                    stage.setMaximized(false); // cancel maximize
                }
            });
        });

    }


    // signIn Button click, Sec Auth
    @FXML
    private void handleLoginAuth(ActionEvent event) {

        String user = username.getText();
        String pass = password.getText();

        if(user.isEmpty() || pass.isEmpty()){
            System.out.println("Username and password are required!");
            return;
        }

        try {

            loginUser(user, pass);


        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText("Authentication Error");
            alert.setContentText(e.getMessage());

            alert.showAndWait();

        }
    }


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DashboardAuthoriseHandler dashboardHandler;

    public void loginUser(String email, String password) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, password);

        // Authenticate user
        Authentication auth = authenticationManager.authenticate(authToken);

        // Redirect to proper dashboard based on role
        Stage stage = (Stage) loginButton.getScene().getWindow();
        dashboardHandler.redirectToDashboard(stage, auth);
    }







//    //Auth Manager Method
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    public void loginUser(String email, String password) {
//
//        //Make a token from login form. Then compare with token stored on db.
//        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
//
//        // THIS takes credentials from form, sends to securityConfig method Authmanager that uses this tokens email
//        // then compares the hash to the token passed here.
//        Authentication auth = authenticationManager.authenticate(authToken);
//
//        // If no exception, login succeeded
//        System.out.println("User Authenticated Successfully with: " + email);
//        System.out.println("logging in");
//        changePage(auth);
//
//
//    }

    @FXML
    private void handleHoverEnter(MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle("-fx-background-color: rgba(69, 200, 255, 1);"); // example hover effect
    }

    @FXML
    private void handleHoverExit(MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle("-fx-background-color: rgba(69, 150, 255, 1);"); // revert to normal
    }


    @FXML
    private void exitApplication(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();  // closes the current window
    }


    //just changes page
    //THIS IS NO LONGER USED OR CALLED




//    @FXML
//    private void changePage(Authentication auth) {
//        try {
//
//            // Determine which FXML to load
//            String role = auth.getAuthorities().stream()
//                    .map(GrantedAuthority::getAuthority)
//                    .findFirst()
//                    .orElse("");
//
//            System.out.println("Role detected: '" + role + "'");
//
//            String fxmlToLoad;
//
//            //THIS IS NO LONGER USED OR CALLED, this change page method is deprecated
//            if ("ADMIN".equals(role)) {
//                fxmlToLoad = "/scenes/dashboardAdmin.fxml";
//            } else if ("MANAGER".equals(role)) {
//                fxmlToLoad = "/scenes/dashboard.fxml";
//            } else if ("USER".equals(role)) {  //
//                fxmlToLoad = "/scenes/dashboardUser.fxml";
//            } else {
//                fxmlToLoad = "/scenes/window-sign-upFinal.fxml"; // fallback
//                System.out.println(fxmlToLoad);
//            }
//
//
//            System.out.println("Loading FXML: " + fxmlToLoad);
//
//            System.out.println("Load dashboard page: " + fxmlToLoad);
//
//            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlToLoad));
//            loader.setControllerFactory(MainFinal.getSpringContext()::getBean);
//            Parent dashboardRoot = loader.load();
//
//            DashboardController controller = loader.getController();
//
//            Stage stage = (Stage) loginButton.getScene().getWindow();
//
//            // Create a new Scene or reuse current scene
//            Scene currentScene = loginButton.getScene();
//
//            // Apply CSS only to dashboard
//            currentScene.getStylesheets().clear(); // optional: remove any old styles
//            currentScene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
//
//            // Swap root to dashboard
//            currentScene.setRoot(dashboardRoot);
//
//            stage.setWidth(1100);
//            stage.setHeight(600);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    @FXML
    private void handleGoBack(ActionEvent event) throws IOException {
        // Load the register window FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/windowSignupFinal.fxml"));
        loader.setControllerFactory(com.college.MainFinal.getSpringContext()::getBean); // spring context aware
        Parent registerRoot = loader.load();

        // Get current stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Replace scene with the register window
        Scene scene = new Scene(registerRoot);
        scene.getStylesheets().add(getClass().getResource("/css/buttonStyle.css").toExternalForm()); // keep styling

        stage.setScene(scene);
        stage.setTitle("HMS - User Login");
        stage.show();
    }

}
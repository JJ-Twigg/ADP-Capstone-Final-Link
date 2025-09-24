package com.college.controller;

import com.college.MainFinal;
import com.college.domain.User;
import com.college.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        }
    }


    //Auth Manager Method
    @Autowired
    private AuthenticationManager authenticationManager;

    public void loginUser(String email, String password) {

        //Make a token from login form. Then compare with token stored on db.
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

        // THIS takes credentials from form, sends to securityConfig method Authmanager that uses this tokens email
        // then compares the hash to the token passed here.
        authenticationManager.authenticate(authToken);

        // If no exception, login succeeded
        System.out.println("User Authenticated Successfully with: " + email);
        System.out.println("logging in");
        changePage();


    }







    //just changes page

    @FXML
    private void changePage() {
        try {

            System.out.println("Load dashboard page");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/dashboard.fxml"));
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean);
            Parent dashboardRoot = loader.load();

            DashboardController controller = loader.getController();

            Stage stage = (Stage) loginButton.getScene().getWindow();

            // Create a new Scene or reuse current scene
            Scene currentScene = loginButton.getScene();

            // Apply CSS only to dashboard
            currentScene.getStylesheets().clear(); // optional: remove any old styles
            currentScene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

            // Swap root to dashboard
            currentScene.setRoot(dashboardRoot);

            stage.setWidth(1100);
            stage.setHeight(600);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleGoBack(ActionEvent event) throws IOException {
        // Load the register window FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-sign-up.fxml"));
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
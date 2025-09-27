package com.college.controller;

import com.college.MainFinal;
import com.college.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserRegisterController {


    @FXML private TextField nameField;
    @FXML private TextField surnameField;
    @FXML private TextField ageField;
    @FXML private TextField genderField;



    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @Autowired
    private UserService userService;

    @FXML
    private ComboBox<String> roleComboBox;








    // Signup button click
    @FXML
    private void handleSignUp(ActionEvent event) {
        String email = username.getText().trim();
        String pass = password.getText();

        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String ageText = ageField.getText().trim();
        String gender = genderField.getText().trim();
        String role = roleComboBox.getValue();

        if (email.isEmpty() || pass.isEmpty() || name.isEmpty() || surname.isEmpty()
                || ageText.isEmpty() || gender.isEmpty()) {
            System.out.println("All fields are required!");
            return;
        }

        Integer age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            System.out.println("Invalid age input!");
            return;
        }

        try {
            userService.register(email, pass, name, surname, age, gender,role);
            System.out.println("User registered successfully!");
            goToLoginPage(event);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @FXML
    private void goToLoginPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-login.fxml"));
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean);

            Parent loginPage = loader.load();
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(loginPage);

            // Optionally add CSS
            String stylesheet = getClass().getResource("/css/buttonStyle.css").toExternalForm();
            if (!scene.getStylesheets().contains(stylesheet)) {
                scene.getStylesheets().add(stylesheet);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-login.fxml"));
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean);

            Parent loginPage = loader.load();
            Stage stage = (Stage) username.getScene().getWindow();
            stage.getScene().setRoot(loginPage);

            String stylesheet = getClass().getResource("/css/buttonStyle.css").toExternalForm();
            Scene scene = stage.getScene();
            if (!scene.getStylesheets().contains(stylesheet)) {
                scene.getStylesheets().add(stylesheet);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}

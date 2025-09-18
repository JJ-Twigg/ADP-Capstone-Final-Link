//package com.college.controller;
//
//import com.college.AppRegister;
//import com.college.service.UserService;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import javax.swing.*;
//import java.io.IOException;
//
//@Component
//public class UserRegisterController {
//
//    @FXML
//    private TextField username;
//
//    @FXML
//    private PasswordField password;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    // get username
//    public String getUsernameInput() {
//        return username.getText().trim();
//    }
//
//    // get password
//    public String getPasswordInput() {
//        return password.getText();
//    }
//
//    // signup button click
//    @FXML
//    private void handleSignUp(ActionEvent event) {
//        String user = username.getText();
//        String pass = password.getText();
//
//        if(user.isEmpty() || pass.isEmpty()){
//            System.out.println("Username and password are required!");
//            return;
//        }
//
//        try {
//            userService.register(user, pass);
//            System.out.println("User registered successfully!");
//            changePage(event);
//
//
//        } catch (Exception e){
//            System.out.println("Error: " + e.getMessage());
//        }
//    }
//
//    public void setUserService(UserService userService) {
//        this.userService = userService;
//    }
//
//
//    @FXML
//    private void changePage(ActionEvent event) {
//        try {
//            System.out.println("Load new page");
//
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-login.fxml"));
//            // Use Spring to create the controller and inject beans
//            loader.setControllerFactory(AppRegister.getSpringContext()::getBean);
//
//            Parent newPage = loader.load();
//
//            Scene currentScene = ((Node) event.getSource()).getScene();
//            currentScene.setRoot(newPage);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}

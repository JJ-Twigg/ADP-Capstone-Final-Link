//package com.college.controller;
//
//import com.college.domain.User;
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
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class UserLoginController {
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
//    public void setUserService(UserService userService) {
//        this.userService = userService;
//    }
//
//    // signup button click
//    @FXML
//    private void handleLogin(ActionEvent event) {
//
//        String user = username.getText();
//        String pass = password.getText();
//
//        if(user.isEmpty() || pass.isEmpty()){
//            System.out.println("Username and password are required!");
//            return;
//        }
//
//        try {
//            User login = userService.login(user, pass);
//
//            if(login!=null){
//                System.out.println("change page");
//
//                //go to dashboard if credentials were correct. user returned is not null
//                //            changePage(event);
//
//                //do we need to implement auth provider, filterChain???
//                //Auth uses tokens. pass and email in a token. and it can compare roles too.
//            }
//
//
//        } catch (Exception e){
//            System.out.println("Error: " + e.getMessage());
//        }
//    }
//
//
//    // signup button click, Manual login system
//    @FXML
//    private void handleLoginAuth(ActionEvent event) {
//
//        String user = username.getText();
//        String pass = password.getText();
//
//        if(user.isEmpty() || pass.isEmpty()){
//            System.out.println("Username and password are required!");
//            return;
//        }
//
//        try {
//
//            loginUser(user, pass);
//
//
//        } catch (Exception e){
//            System.out.println("Error: " + e.getMessage());
//        }
//    }
//
//
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
//        authenticationManager.authenticate(authToken);
//
//        // If no exception, login succeeded
//        System.out.println("User Authenticated Successfully with: " + email);
//        System.out.println("logging in");
//    }
//
//
//    @FXML
//    private void changePage(ActionEvent event) {
//        try {
//
//            System.out.println(" load new page");
//
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-dashboard.fxml"));
//            Parent newPage = loader.load();
//
//            UserLoginController controller = loader.getController();
////            controller.setUserService(this.userService);
//
//            Scene currentScene = ((Node) event.getSource()).getScene();
//            currentScene.setRoot(newPage);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
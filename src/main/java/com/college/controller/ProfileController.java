package com.college.controller;

import com.college.domain.User;
import com.college.repository.UserRepository;
import com.college.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.tomcat.util.net.openssl.OpenSSLStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.Optional;

import static org.apache.tomcat.util.net.openssl.OpenSSLStatus.setName;

@Component
public class ProfileController {
    @FXML
    private Label  userEmailLabel;



    @FXML
    private Label nameLabel;

    @FXML
    private ImageView profileImageView;

    @Autowired
    private UserService userService;

    private String userEmail;

    public void setUserEmail(String email) {
        this.userEmail = email;

        if (userEmailLabel != null) {
            userEmailLabel.setText(email);


        }
    }

    public void setUserName(String name) {
        if (nameLabel != null) nameLabel.setText(name);
    }

    // --------------------------------------

@Autowired
    UserRepository userRepository;

    @FXML
    private void updateEmail() {
        System.out.println("\nonMouseClicked");

        Optional<User> userObj = userRepository.findByEmail(userEmailLabel.getText());
        System.out.println("User: " + userObj);

        TextInputDialog dialog = new TextInputDialog(userEmailLabel.getText());
        dialog.setTitle("Update Email");
        dialog.setHeaderText("Update Email");

        Optional<String> email = dialog.showAndWait();
        if (email.isEmpty()) return;

        userObj.get().setEmail(email.get());
        setUserEmail(email.get());

        userRepository.save(userObj.get());
        System.out.println("User updated");
        System.out.println(userObj.get());
    }

    @FXML
    private void updateName() {
        System.out.println("\nonMouseClicked");
        String nameString = nameLabel.getText();

        Optional<User> userObj = userRepository.findByEmail(userEmailLabel.getText());
        System.out.println("User: " + userObj);

//        TextInputDialog dialog = new TextInputDialog();
        TextInputDialog dialog = new TextInputDialog(nameString);
        dialog.setTitle("Update Name");
        dialog.setHeaderText("Update Name");

        Optional<String> name = dialog.showAndWait();
        if (name.isEmpty()) return;

        userObj.get().setName(name.get());
        setName(OpenSSLStatus.Name.valueOf(name.get()));

        userRepository.save(userObj.get());
        System.out.println("User updated");
        System.out.println(userObj.get());
    }
    // --------------------------------------


    @FXML
    public void initialize() {
        // Optional: if setUserEmail was called before initialize
        if (userEmail != null && userEmailLabel != null) {
            userEmailLabel.setText(userEmail);

            byte[] imageBytes = userService.getUserPhoto(userEmail);
            if (imageBytes != null) {
                profileImageView.setImage(new Image(new ByteArrayInputStream(imageBytes)));
            }
        }
    }
}

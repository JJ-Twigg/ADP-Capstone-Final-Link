package com.college.controller;

import com.college.domain.User;
import com.college.repository.UserRepository;
import com.college.service.UserService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class ProfileController {

    @FXML private Label userEmailLabel;
    @FXML private Label clockLabel;
    @FXML private Label nameLabel;
    @FXML private ImageView profileImageView;

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;

    private String userEmail; // currently logged-in user

    // -----------------------------
    public void setUserEmail(String email) {
        this.userEmail = email;
        refreshUserInfo();
    }

    // -----------------------------
    @FXML
    private void updateEmail() {
        Optional<User> userOpt = userRepository.findByEmail(userEmail);
        if (userOpt.isEmpty()) return;

        User user = userOpt.get();
        TextInputDialog dialog = new TextInputDialog(user.getEmail());
        dialog.setTitle("Update Email");
        dialog.setHeaderText("Update Email");

        Optional<String> newEmail = dialog.showAndWait();
        if (newEmail.isEmpty()) return;

        // Save new email
        user.setEmail(newEmail.get());
        userRepository.save(user);

        // Update current userEmail and refresh
        this.userEmail = newEmail.get();
        refreshUserInfo();
    }

    @FXML
    private void updateName() {
        Optional<User> userOpt = userRepository.findByEmail(userEmail);
        if (userOpt.isEmpty()) return;

        User user = userOpt.get();
        TextInputDialog dialog = new TextInputDialog(user.getName());
        dialog.setTitle("Update Name");
        dialog.setHeaderText("Update Name");

        Optional<String> newName = dialog.showAndWait();
        if (newName.isEmpty()) return;

        // Save new name
        user.setName(newName.get());
        userRepository.save(user);

        // Refresh labels
        refreshUserInfo();
    }

    // -----------------------------
    @FXML
    public void initialize() {
        startClock();
        refreshUserInfo();
    }

    private void refreshUserInfo() {
        if (userEmail == null) return;

        Optional<User> userOpt = userRepository.findByEmail(userEmail);
        if (userOpt.isEmpty()) return;

        User user = userOpt.get();

        // Update labels
        if (userEmailLabel != null) userEmailLabel.setText(user.getEmail());
        if (nameLabel != null) nameLabel.setText(user.getName());

        // Update profile image
        byte[] imageBytes = userService.getUserPhoto(user.getEmail());
        if (imageBytes != null && profileImageView != null) {
            profileImageView.setImage(new Image(new ByteArrayInputStream(imageBytes)));
        }
    }

    private void startClock() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM dd yyyy  HH:mm:ss");
        Timeline clock = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    if (clockLabel != null) clockLabel.setText(LocalDateTime.now().format(formatter));
                }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }
}

package com.college.controller;

import com.college.MainFinal;
import com.college.domain.Guest;
import com.college.repository.GuestRepository;
import com.college.service.GuestUIServiceNaked;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;

public class AddEditGuestDialog extends Dialog<Guest> {

    private final GuestUIServiceNaked guestService;

    private final TextField txtID = new TextField();
    private final TextField txtName = new TextField();
    private final TextField txtSurname = new TextField();
    private final TextField txtEmail = new TextField();
    private final TextField txtContact = new TextField();
    private final ComboBox<String> cbPayment = new ComboBox<>();


    private Guest savedGuest; // add this field

    public Guest getSavedGuest() {
        return savedGuest;
    }


    private Runnable onSaveCallback;

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }




    public AddEditGuestDialog(GuestRepository guestRepository, Guest guest) {
        this.guestService = new GuestUIServiceNaked(guestRepository);

        setTitle(guest == null ? "Add Guest" : "Edit Guest");



        // Header label
        Label headerLabel = new Label(guest == null ? "Add Guest" : "Edit Guest");
        headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        HBox headerBox = new HBox(headerLabel);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(0, 0, 10, 0));
        getDialogPane().setHeader(headerBox);

        // Buttons
        ButtonType submitButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(submitButtonType, cancelButtonType);

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));

        // Fields
        grid.add(new Label("Guest ID:"), 0, 0);
        grid.add(txtID, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(txtName, 1, 1);
        grid.add(new Label("Surname:"), 0, 2);
        grid.add(txtSurname, 1, 2);
        grid.add(new Label("Email:"), 0, 3);
        grid.add(txtEmail, 1, 3);
        grid.add(new Label("Contact Number:"), 0, 4);
        grid.add(txtContact, 1, 4);
        grid.add(new Label("Payment Method:"), 0, 5);
        grid.add(cbPayment, 1, 5);

        // Styles
        String textFieldStyle = "-fx-pref-width: 250px; -fx-padding: 6px;";
        txtID.setStyle(textFieldStyle);
        txtName.setStyle(textFieldStyle);
        txtSurname.setStyle(textFieldStyle);
        txtEmail.setStyle(textFieldStyle);
        txtContact.setStyle(textFieldStyle);
        cbPayment.setStyle(textFieldStyle);

        // Payment methods
        cbPayment.getItems().addAll("Cash", "Credit Card", "Debit Card", "EFT", "PayPal");
        cbPayment.setEditable(false);

        // Pre-fill or generate default values
        if (guest != null) {
            txtID.setText(String.valueOf(guest.getGuestID()));
            txtID.setDisable(true);
            txtName.setText(guest.getName());
            txtSurname.setText(guest.getSurname());
            txtEmail.setText(guest.getEmail());
            txtContact.setText(formatPhone(guest.getContactNumber()));
            cbPayment.setValue(guest.getPaymentDetails());
        } else {
            txtID.setText("Auto"); // Hibernate generates ID
            txtID.setDisable(true);
            txtEmail.setText("guest@example.com");
            txtContact.setText("000 000 0000");
        }

        // Auto-format phone number
        txtContact.textProperty().addListener((obs, oldVal, newVal) -> {
            String digits = newVal.replaceAll("\\D", "");
            if (digits.length() > 10) digits = digits.substring(0, 10);
            StringBuilder formatted = new StringBuilder();
            for (int i = 0; i < digits.length(); i++) {
                if (i == 3 || i == 6) formatted.append(" ");
                formatted.append(digits.charAt(i));
            }
            if (!formatted.toString().equals(newVal)) txtContact.setText(formatted.toString());
        });

        // Auto-capitalize Name & Surname
        txtName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && !txtName.getText().isEmpty()) txtName.setText(capitalizeFirst(txtName.getText()));
        });
        txtSurname.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && !txtSurname.getText().isEmpty()) txtSurname.setText(capitalizeFirst(txtSurname.getText()));
        });

        // Content
        getDialogPane().setContent(grid);

        // Dialog button styles
        getDialogPane().lookupButton(submitButtonType).setStyle("-fx-background-color: #26AD00; -fx-text-fill: white;");
        getDialogPane().lookupButton(cancelButtonType).setStyle("-fx-background-color: #C20010; -fx-text-fill: white;");

        // Validation
        Button saveButton = (Button) getDialogPane().lookupButton(submitButtonType);
        saveButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            String name = capitalizeFirst(txtName.getText().trim());
            String surname = capitalizeFirst(txtSurname.getText().trim());
            if (name.isEmpty() || surname.isEmpty()) {
                showAlert("Validation Error", "Name and Surname cannot be empty.");
                event.consume();
                return;
            }
            String email = txtEmail.getText().trim();
            if (!email.contains("@") || email.length() < 5) {
                showAlert("Validation Error", "Email must be valid and contain '@'.");
                event.consume();
                return;
            }
            String rawContact = txtContact.getText().replaceAll("\\s", "");
            if (!rawContact.matches("\\d{10}")) {
                showAlert("Validation Error", "Contact number must be exactly 10 digits.");
                event.consume();
                return;
            }
            String payment = cbPayment.getValue();
            if (payment == null || payment.isEmpty()) {
                showAlert("Validation Error", "Please select a payment method.");
                event.consume();
            }

            // Email uniqueness
            try {
                List<Guest> allGuests = guestService.getAllGuests();
                boolean exists = allGuests.stream()
                        .anyMatch(g -> g.getEmail().equalsIgnoreCase(email)
                                && (guest == null || g.getGuestID() != guest.getGuestID()));
                if (exists) {
                    showAlert("Validation Error", "A guest with this Email already exists.");
                    event.consume();
                }
            } catch (Exception ignored) {}
        });

        setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                String name = capitalizeFirst(txtName.getText().trim());
                String surname = capitalizeFirst(txtSurname.getText().trim());
                String email = txtEmail.getText().trim();
                String formattedContact = txtContact.getText().replaceAll("\\s", "")
                        .replaceFirst("(\\d{3})(\\d{3})(\\d{4})", "$1 $2 $3");
                String payment = cbPayment.getValue();

                Guest.GuestBuilder builder = new Guest.GuestBuilder()
                        .setName(name)
                        .setSurname(surname)
                        .setEmail(email)
                        .setContactNumber(formattedContact)
                        .setPaymentDetails(payment);

                if (guest != null) {
                    builder.setGuestID(guest.getGuestID());
                }

                savedGuest = builder.build(); // store built Guest
                return savedGuest;
            }
            return null;
        });
    }






    public void openReservationPage(Guest guest) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/reservationFinal.fxml"));
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean);
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Reservation");

            // Pass the Guest object to ReservationUIController
            ReservationUIController controller = loader.getController();
            controller.setGuest(guest); // now passing full Guest object

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Reservation page: " + e.getMessage());
            alert.showAndWait();
        }
    }











    private String capitalizeFirst(String text) {
        text = text.trim();
        if (text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    private String formatPhone(String number) {
        String digits = number.replaceAll("\\D", "");
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < digits.length(); i++) {
            if (i == 3 || i == 6) formatted.append(" ");
            formatted.append(digits.charAt(i));
        }
        return formatted.toString();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

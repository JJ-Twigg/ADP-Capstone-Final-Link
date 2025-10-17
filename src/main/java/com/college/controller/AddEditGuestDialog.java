package com.college.controller;

import com.college.domain.Guest;
import com.college.repository.GuestRepository;
import com.college.service.GuestUIServiceNaked;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class AddEditGuestDialog extends Dialog<Guest> {

    private final GuestUIServiceNaked guestService;

    private final TextField txtID = new TextField();
    private final TextField txtName = new TextField();
    private final TextField txtSurname = new TextField();
    private final TextField txtEmail = new TextField();
    private final TextField txtContact = new TextField();
    private final ComboBox<String> cbPayment = new ComboBox<>();

    private Guest savedGuest;

    public Guest getSavedGuest() { return savedGuest; }

    private Runnable onSaveCallback;
    public void setOnSaveCallback(Runnable callback) { this.onSaveCallback = callback; }

    public AddEditGuestDialog(GuestRepository guestRepository, Guest guest) {
        this.guestService = new GuestUIServiceNaked(guestRepository);
        setTitle(guest == null ? "Add Guest" : "Edit Guest");

        // Header
        Label headerLabel = new Label(guest == null ? "Add Guest" : "Edit Guest");
        headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill:#66BA88");
        HBox headerBox = new HBox(headerLabel);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(0, 0, 10, 0));

        // Grid for input fields (no labels, using promptText)
        // Grid for input fields with labels
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));

// Add labels and inputs
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


        grid.add(cbPayment, 1, 5);

        // Input field styles and prompt text
        String inputStyle = "-fx-pref-width: 260px; -fx-padding: 6px; -fx-background-radius: 0; -fx-border-radius: 0;";
        txtID.setStyle(inputStyle);
        txtName.setStyle(inputStyle);
        txtSurname.setStyle(inputStyle);
        txtEmail.setStyle(inputStyle);
        txtContact.setStyle(inputStyle);
        cbPayment.setStyle(inputStyle);

        txtID.setPromptText("Guest ID");
        txtName.setPromptText("Name");
        txtSurname.setPromptText("Surname");
        txtEmail.setPromptText("Email");
        txtContact.setPromptText("Contact Number");
        cbPayment.setPromptText("Select Payment Method");

        cbPayment.getItems().addAll("Cash", "Credit Card", "Debit Card", "EFT", "PayPal");
        cbPayment.setEditable(false);

        // Prefill values
        if (guest != null) {
            txtID.setText(String.valueOf(guest.getGuestID()));
            txtID.setDisable(true);
            txtName.setText(guest.getName());
            txtSurname.setText(guest.getSurname());
            txtEmail.setText(guest.getEmail());
            txtContact.setText(formatPhone(guest.getContactNumber()));
            cbPayment.setValue(guest.getPaymentDetails());
        } else {
            txtID.setText("Auto");
            txtID.setDisable(true);
            txtEmail.setText("guest@example.com");
            txtContact.setText("000 000 0000");
        }

        // Format phone input
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

        // Auto-capitalize names
        txtName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && !txtName.getText().isEmpty())
                txtName.setText(capitalizeFirst(txtName.getText()));
        });
        txtSurname.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && !txtSurname.getText().isEmpty())
                txtSurname.setText(capitalizeFirst(txtSurname.getText()));
        });

        // Centered buttons with FXML style
        Button saveButton = new Button("Save");
        saveButton.setPrefSize(120, 40);
        saveButton.setStyle("-fx-background-color: #66BA88; -fx-text-fill: white; -fx-background-radius: 0;");
        saveButton.setCursor(Cursor.HAND);

        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefSize(120, 40);
        cancelButton.setStyle("-fx-background-color: #cc000a; -fx-text-fill: white; -fx-background-radius: 0;");
        cancelButton.setCursor(Cursor.HAND);

        HBox buttonBox = new HBox(20, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(20, headerBox, grid, buttonBox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        getDialogPane().setContent(root);

        cancelButton.setOnAction(e -> {
            setResult(null); // optional, for completeness
            getDialogPane().getScene().getWindow().hide(); // actually closes the dialog
        });

        // Cancel closes dialog
        saveButton.setOnAction(e -> {
            String name = capitalizeFirst(txtName.getText().trim());
            String surname = capitalizeFirst(txtSurname.getText().trim());
            String email = txtEmail.getText().trim();
            String contact = txtContact.getText().replaceAll("\\s", "");
            String payment = cbPayment.getValue();

            // Validation
            if (name.isEmpty() || surname.isEmpty()) { showAlert("Validation Error", "Name and Surname cannot be empty."); return; }
            if (!email.contains("@") || email.length() < 5) { showAlert("Validation Error", "Email must be valid."); return; }
            if (!contact.matches("\\d{10}")) { showAlert("Validation Error", "Contact must be exactly 10 digits."); return; }
            if (payment == null || payment.isEmpty()) { showAlert("Validation Error", "Please select a payment method."); return; }

            try {
                List<Guest> allGuests = guestService.getAllGuests();
                boolean exists = allGuests.stream()
                        .anyMatch(g -> g.getEmail().equalsIgnoreCase(email)
                                && (guest == null || g.getGuestID() != guest.getGuestID()));
                if (exists) { showAlert("Validation Error", "A guest with this email already exists."); return; }
            } catch (Exception ignored) {}

            // Confirmation alert
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Save");
            confirm.setHeaderText(null);
            confirm.setContentText("Are you sure you want to save this guest?");


            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Build guest and save
                    Guest.GuestBuilder builder = new Guest.GuestBuilder()
                            .setName(name)
                            .setSurname(surname)
                            .setEmail(email)
                            .setContactNumber(txtContact.getText())
                            .setPaymentDetails(payment);

                    if (guest != null) builder.setGuestID(guest.getGuestID());
                    savedGuest = builder.build();
                    if (onSaveCallback != null) onSaveCallback.run();
                    setResult(savedGuest);

                    // Close dialog safely
                    this.close();
                }
            });
        });

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

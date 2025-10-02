package com.college.controller;

import com.college.domain.Guest;
import com.college.domain.Payment;
import com.college.service.PaymentService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PaymentFormController {

    @FXML private TextField txtAmount;
    @FXML private ChoiceBox<String> choiceMethod;
    @FXML private ChoiceBox<String> choiceStatus;
    @FXML private DatePicker datePicker;

    @Autowired
    private PaymentService paymentService;

    private Payment payment;



    //FK TO GUEST
    private Guest guest;

    public void setGuest(Guest guest) {
        this.guest = guest;
    }




    @FXML
    public void initialize() {
        choiceMethod.setValue("Select Payment Method");
        choiceMethod.getItems().addAll("Cash", "Card", "EFT");

        choiceStatus.setValue("Select Payment Status");
        choiceStatus.getItems().addAll("Pending", "Completed", "Failed");
        datePicker.setValue(LocalDate.now());

        // Debug: Check if Spring injection worked
        System.out.println("PaymentFormController initialized");
        System.out.println("PaymentService injected: " + (paymentService != null));
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
        if (payment != null) {
            txtAmount.setText(String.valueOf(payment.getPaymentAmount()));
            choiceMethod.setValue(payment.getPaymentMethod());
            choiceStatus.setValue(payment.getPaymentStatus());
            datePicker.setValue(payment.getPaymentDate());
        }
    }

    @FXML
    private void handleSave() {
        try {
            // Validate input
            if (txtAmount.getText().trim().isEmpty()) {
                showAlert("Please enter an amount");
                return;
            }

            if (choiceMethod.getValue() == null) {
                showAlert("Please select a payment method");
                return;
            }

            if (choiceStatus.getValue() == null) {
                showAlert("Please select a payment status");
                return;
            }

            if (datePicker.getValue() == null) {
                showAlert("Please select a date");
                return;
            }

            double amount = Double.parseDouble(txtAmount.getText().trim());

            if (payment == null) {
                // Create new payment
                Payment newPayment = new Payment.Builder()
                        .setPaymentAmount(amount)
                        .setPaymentMethod(choiceMethod.getValue())
                        .setPaymentStatus(choiceStatus.getValue())
                        .setPaymentDate(datePicker.getValue())
                        .build();

                newPayment.setGuest(guest);

                paymentService.create(newPayment);

                showAlert(Alert.AlertType.INFORMATION, "Payment created successfully!");


            } else {
                // Update existing payment
                payment.setPaymentAmount(amount);
                payment.setPaymentMethod(choiceMethod.getValue());
                payment.setPaymentStatus(choiceStatus.getValue());
                payment.setPaymentDate(datePicker.getValue());

                paymentService.update(payment);
                showAlert(Alert.AlertType.INFORMATION, "Payment updated successfully!");
            }

            closeWindow();

        } catch (NumberFormatException e) {
            showAlert("Please enter a valid numeric amount");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error saving payment: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage modalStage = (Stage) txtAmount.getScene().getWindow();  // the modal
        Stage parentStage = (Stage) modalStage.getOwner();            // PaymentView window

        modalStage.close();  // close the modal
        if (parentStage != null) {
            parentStage.close();  // close PaymentView
        }
    }

    private void showAlert(String message) {
        showAlert(Alert.AlertType.WARNING, message);
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.showAndWait();
    }
}
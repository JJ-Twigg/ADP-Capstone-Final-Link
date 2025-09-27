package com.college.controller;

import com.college.domain.Guest;
import com.college.domain.Payment;
import com.college.service.PaymentService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class PaymentViewController {

    @FXML private TableView<Payment> paymentTable;
    @FXML private TableColumn<Payment, Integer> colId;
    @FXML private TableColumn<Payment, Double> colAmount;
    @FXML private TableColumn<Payment, String> colMethod;
    @FXML private TableColumn<Payment, String> colStatus;
    @FXML private TableColumn<Payment, LocalDate> colDate;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ApplicationContext applicationContext;

    private ObservableList<Payment> payments = FXCollections.observableArrayList();



    //FK TO GUEST
    private Guest guest;

    public void setGuest(Guest guest) {
        this.guest = guest;
    }







    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        colMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        paymentTable.setItems(payments);

        // Debug: Check if Spring injection worked
        System.out.println("PaymentViewController initialized");
        System.out.println("PaymentService injected: " + (paymentService != null));
        System.out.println("ApplicationContext injected: " + (applicationContext != null));

        loadPayments();
    }

    private void loadPayments() {
        try {
            List<Payment> list = paymentService.getAll();
            Platform.runLater(() -> {
                payments.clear();
                payments.addAll(list);
            });
        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> showAlert("Error loading payments: " + e.getMessage()));
        }
    }

    @FXML
    private void handleAddPayment() {
        openForm(null);
    }

    @FXML
    private void handleUpdatePayment() {
        Payment selected = paymentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openForm(selected);
        } else {
            showAlert("Please select a payment to update.");
        }
    }

    @FXML
    private void handleDeletePayment() {
        Payment selected = paymentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete this payment?",
                    ButtonType.YES, ButtonType.NO);

            if (confirmAlert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                try {
                    paymentService.delete(selected.getPaymentId());
                    showAlert(Alert.AlertType.INFORMATION, "Payment deleted successfully!");
                    loadPayments();
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Error deleting payment: " + e.getMessage());
                }
            }
        } else {
            showAlert("Please select a payment to delete.");
        }
    }

    private void openForm(Payment payment) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dialog_boxes/add-payment.fxml"));

            // This is the key fix: Use Spring to create the controller
            loader.setControllerFactory(applicationContext::getBean);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(payment == null ? "Add Payment" : "Update Payment");
            stage.setScene(new Scene(loader.load()));

            PaymentFormController controller = loader.getController();
            controller.setPayment(payment);

            controller.setGuest(this.guest);

            stage.showAndWait();
            loadPayments(); // Refresh the table
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error opening form: " + e.getMessage());
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
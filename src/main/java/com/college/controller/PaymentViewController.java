package com.college.controller;

import com.college.domain.Guest;
import com.college.domain.Payment;
import com.college.repository.PaymentRepository;
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

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentViewController {

    @FXML private TableView<Payment> paymentTable;
    @FXML private TableColumn<Payment, Integer> colId;
    @FXML private TableColumn<Payment, Integer> colGuestId;

    @FXML private TableColumn<Payment, Double> colAmount;
    @FXML private TableColumn<Payment, String> colMethod;
    @FXML private TableColumn<Payment, String> colStatus;
    @FXML private TableColumn<Payment, LocalDate> colDate;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    PaymentRepository paymentRepository;

    private ObservableList<Payment> payments = FXCollections.observableArrayList();



    //FK TO GUEST
    private Guest guest;

    public void setGuest(Guest guest) {
        this.guest = guest;
    }


    private double price;

    public void setPrice(double price) {
        this.price = price;
        System.out.println("Paymentview Controller price: " + price);
    }





    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        colMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        colGuestId.setCellValueFactory(cellData -> {
            Payment payment = cellData.getValue();
            return new javafx.beans.property.SimpleIntegerProperty(
                    payment.getGuest() != null ? payment.getGuest().getGuestId() : 0
            ).asObject();
        });


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
    private void handleUpdatePayment() {
        Payment selectedPayment = paymentTable.getSelectionModel().getSelectedItem();
        if (selectedPayment != null) {
            // Grab the Guest from the payment
            this.guest = selectedPayment.getGuest();
            openForm(selectedPayment);
        } else {
            showAlert("Please select a payment to update.");
        }
    }

    @FXML
    private void handleAddPayment() {
        if (this.guest == null) {
            showAlert("Please select a payment first to know the guest.");
            return;
        }
        openForm(null); // will use this.guest
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
            loader.setControllerFactory(applicationContext::getBean);

            Stage stage = new Stage();
            stage.initOwner((Stage) paymentTable.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setTitle(payment == null ? "Add Payment" : "Update Payment");
            stage.setScene(new Scene(loader.load()));

            PaymentFormController controller = loader.getController();

            controller.setGuest(this.guest);  // Guest reference

            if (payment != null) {
                controller.setPayment(payment); // <-- This tells the modal we’re editing
            } else {
                controller.setPrice(this.price);
            }

            stage.showAndWait();
            loadPayments(); // Refresh table after modal closes
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error opening form: " + e.getMessage());
        }
    }




    @FXML
    private void handlePrintAllPayments() {
        try {
            // Fetch all payments from your service
            List<Payment> payments = paymentService.getAll();

            if (payments.isEmpty()) {
                System.out.println("No payments found.");
                return;
            }

            // Define folder and file
            String folderPath = "W:/Work/IntelliJ IDEA Community Edition 2025.2.1/projects/ADP3_capstone_project/records";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String timestamp = LocalDateTime.now().format(formatter);
            String fileName = "payments_" + timestamp + ".txt";

            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs(); // create folder if it doesn't exist
            }

            File file = new File(folder, fileName);

            try (FileWriter writer = new FileWriter(file)) {
                for (Payment payment : payments) {
                    writer.write(payment.toString() + System.lineSeparator());
                }
            }


            System.out.println("Payments saved to: " + file.getAbsolutePath());

            showSuccess("Payments Successfully saved to Records folder");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching payments: " + e.getMessage());
        }
    }

    @FXML
    private void searchTopPayments() {
        try {

            Pageable top3 = PageRequest.of(0, 3);
            List<Guest> topGuests = paymentRepository.findTopRevenueGeneratingGuests(top3);

            if (topGuests.isEmpty()) {
                showAlert("No payments found.");
                paymentTable.getItems().clear();
                return;
            }

            // Option 1: Show payments for these guests
            List<Payment> topPayments = new ArrayList<>();
            for (Guest guest : topGuests) {
                topPayments.addAll(paymentService.getPaymentsByGuest(guest));
            }

            // Sort by payment amount descending
            topPayments.sort((p1, p2) -> Double.compare(p2.getPaymentAmount(), p1.getPaymentAmount()));

            // Update table
            payments.clear();
            payments.addAll(topPayments);
            paymentTable.setItems(payments);

            showSuccess("Top 3 revenue-generating guests displayed.");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error fetching top payments: " + e.getMessage());
        }
    }




    private void showAlert(String message) {
        showAlert(Alert.AlertType.WARNING, message);
    }

    private void showSuccess(String message) {
        showAlert(Alert.AlertType.INFORMATION, message);
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.showAndWait();
    }
}
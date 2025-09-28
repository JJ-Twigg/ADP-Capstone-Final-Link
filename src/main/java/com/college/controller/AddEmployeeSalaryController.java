package com.college.controller;

import com.college.domain.EmployeeSalary;
import com.college.service.EmployeeSalaryService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AddEmployeeSalaryController {

    @FXML private TextField txtAmount;
    @FXML private ChoiceBox<String> choiceMethod;
    @FXML private DatePicker datePicker;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    private EmployeeSalary employeeSalary;

    @Autowired
    private EmployeeSalaryService employeeSalaryService;

    @FXML
    public void initialize() {
        choiceMethod.getItems().addAll("Cash", "Card", "EFT");
        datePicker.setValue(LocalDate.now());
    }

    public void setEmployeeSalary(EmployeeSalary employeeSalary) {
        this.employeeSalary = employeeSalary;
        if (employeeSalary != null) {
            txtAmount.setText(String.valueOf(employeeSalary.getAmount()));
            choiceMethod.setValue(employeeSalary.getMethod());
            datePicker.setValue(employeeSalary.getDate());
        }
    }

    @FXML
    private void handleSave() {
        try {
            if (txtAmount.getText().trim().isEmpty()) {
                showAlert("Please enter an amount");
                return;
            }
            if (choiceMethod.getValue() == null) {
                showAlert("Please select a payment method");
                return;
            }
            if (datePicker.getValue() == null) {
                showAlert("Please select a date");
                return;
            }

            double amount = Double.parseDouble(txtAmount.getText().trim());

            if (employeeSalary == null) {
                EmployeeSalary newSalary = new EmployeeSalary.Builder()
                        .setAmount(amount)
                        .setMethod(choiceMethod.getValue())
                        .setDate(datePicker.getValue())
                        .build();
                employeeSalaryService.create(newSalary);
                showAlert(Alert.AlertType.INFORMATION, "Employee salary created successfully");
            } else {
                employeeSalary.setAmount(amount);
                employeeSalary.setMethod(choiceMethod.getValue());
                employeeSalary.setDate(datePicker.getValue());
                employeeSalaryService.update(employeeSalary);
                showAlert(Alert.AlertType.INFORMATION, "Employee salary updated successfully");
            }

            closeWindow();
        } catch (NumberFormatException e) {
            showAlert("Invalid amount format");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error saving employee salary: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) txtAmount.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        showAlert(Alert.AlertType.WARNING, message);
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.showAndWait();
    }
}

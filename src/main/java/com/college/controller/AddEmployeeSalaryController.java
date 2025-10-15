package com.college.controller;

import com.college.domain.Employee;
import com.college.domain.EmployeeSalary;
import com.college.service.EmployeeSalaryService;
import com.college.service.EmployeeService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

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
    private ComboBox<Employee> employeeComboBox;

    @Autowired
    private EmployeeService empService;


    // newer //
    @FXML
    private Label headerLabel;


    @FXML
    public void initialize() {
        choiceMethod.getItems().addAll("Cash", "Card", "EFT");
        datePicker.setValue(LocalDate.now());

        // Load employees into ComboBox
        try {
            var employees = empService.getAllEmployees();
            employeeComboBox.setItems(FXCollections.observableArrayList(employees));

            // Show full name in the list
            employeeComboBox.setCellFactory(cb -> new ListCell<>() {
                @Override
                protected void updateItem(Employee emp, boolean empty) {
                    super.updateItem(emp, empty);
                    setText(empty || emp == null ? "" : emp.getUser().getName() + " " + emp.getUser().getSurname());
                }
            });

            // Show selected employee in button
            employeeComboBox.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Employee emp, boolean empty) {
                    super.updateItem(emp, empty);
                    setText(empty || emp == null ? "" : emp.getUser().getName() + " " + emp.getUser().getSurname());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error loading employees: " + e.getMessage());
        }
    }

//    public void setEmployeeSalary(EmployeeSalary employeeSalary) {
//        this.employeeSalary = employeeSalary;
//        if (employeeSalary != null) {
//            txtAmount.setText(String.valueOf(employeeSalary.getAmount()));
//            choiceMethod.setValue(employeeSalary.getMethod());
//            datePicker.setValue(employeeSalary.getDate());
//        }
//    }

    public void setEmployeeSalary(EmployeeSalary employeeSalary) {
        this.employeeSalary = employeeSalary; // <--- MUST assign here

        if (employeeSalary == null) {
            headerLabel.setText("Add Employee Salary");
        } else {
            headerLabel.setText("Update Employee Salary");

            txtAmount.setText(String.valueOf(employeeSalary.getAmount()));
            choiceMethod.setValue(employeeSalary.getMethod());
            datePicker.setValue(employeeSalary.getDate());

            // Automatically select the employee in the ComboBox
            employeeComboBox.getSelectionModel().select(employeeSalary.getEmployee());
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

            // CHECK IF employee was selected
            Employee selectedEmployee = employeeComboBox.getSelectionModel().getSelectedItem();
            if (selectedEmployee == null) {
                showAlert("Please select an employee");
                return;
            }

            if (employeeSalary == null) {
                // ADDING NEW SALARY
                // Check if employee already has a salary
                EmployeeSalary existingSalary = employeeSalaryService.findByEmployee(selectedEmployee);
                if (existingSalary != null) {
                    showAlert("This employee already has a salary assigned!");
                    return;
                }

                EmployeeSalary newSalary = new EmployeeSalary.Builder()
                        .setAmount(amount)
                        .setMethod(choiceMethod.getValue())
                        .setDate(datePicker.getValue())
                        .build();

                newSalary.setEmployee(selectedEmployee);
                employeeSalaryService.create(newSalary);
                showAlert(Alert.AlertType.INFORMATION, "Employee salary created successfully");

            } else {
                // UPDATING EXISTING SALARY
                employeeSalary.setAmount(amount);
                employeeSalary.setMethod(choiceMethod.getValue());
                employeeSalary.setDate(datePicker.getValue());
                employeeSalary.setEmployee(selectedEmployee); // keep FK updated
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
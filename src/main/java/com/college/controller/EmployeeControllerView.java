package com.college.controller;

import com.college.domain.Employee;
import com.college.repository.EmployeeRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class EmployeeControllerView {

    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, Integer> colEmployeeId;
    @FXML
    private TableColumn<Employee, String> colJobType;
    @FXML
    private TableColumn<Employee, LocalDate> colStartDate;
    @FXML
    private TableColumn<Employee, Integer> colUserId;

    @Autowired
    private EmployeeRepository repo;

    private ObservableList<Employee> employees = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colJobType.setCellValueFactory(new PropertyValueFactory<>("jobType"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colUserId.setCellValueFactory(cellData -> {
            Employee emp = cellData.getValue();
            return new javafx.beans.property.SimpleIntegerProperty(
//                emp.getUser() != null ? emp.getUser().getId() : 0
            ).asObject();
        });

        loadEmployees();
    }

    private void loadEmployees() {
        employees.clear();
        employees.addAll(repo.findAll());
        employeeTable.setItems(employees);
    }

    @FXML
    private void addEmployee() {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Add Employee");
        dialog.setHeaderText("Job Type:");
        Optional<String> jobTypeResult = dialog.showAndWait();
        if (jobTypeResult.isEmpty()) return;

        dialog.setHeaderText("Start Date (yyyy-MM-dd):");
        Optional<String> startDateResult = dialog.showAndWait();
        if (startDateResult.isEmpty()) return;

        // For simplicity, user is set to null here. You can add user selection logic if needed.
        Employee employee = new Employee(
            jobTypeResult.get(),
            LocalDate.parse(startDateResult.get()),
            null
        );
        repo.save(employee);
        loadEmployees();
    }

    @FXML
    private void updateEmployee() {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select an employee to update.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selected.getJobType());
        dialog.setTitle("Update Employee");
        dialog.setHeaderText("Update Job Type:");
        Optional<String> jobTypeResult = dialog.showAndWait();
        if (jobTypeResult.isEmpty()) return;

        dialog.setHeaderText("Update Start Date (yyyy-MM-dd):");
        Optional<String> startDateResult = dialog.showAndWait();
        if (startDateResult.isEmpty()) return;

        selected.setJobType(jobTypeResult.get());
        selected.setStartDate(LocalDate.parse(startDateResult.get()));
        repo.save(selected);
        loadEmployees();
    }

    @FXML
    private void deleteEmployee() {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select an employee to delete.");
            return;
        }
        repo.deleteById(selected.getEmployeeId());
        loadEmployees();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
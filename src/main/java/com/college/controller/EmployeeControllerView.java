package com.college.controller;

import com.college.AppEmployee;
import com.college.domain.Employee;
import com.college.domain.subclasses.FoodWorker;
import com.college.repository.EmployeeRepository;
import com.college.service.EmployeeService;
import com.college.service.IFoodWorkerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
public class EmployeeControllerView {

    @FXML
    private TableView<Employee> foodWorkerTable;
    @FXML
    private TableColumn<Employee, Integer> colId;
    @FXML
    private TableColumn<Employee, String> colType;
    @FXML
    private TableColumn<Employee, String> colSpecialization;

    @FXML
    private TableColumn<Employee, LocalTime> time;

    @Autowired
    private EmployeeRepository repo;

    @Autowired
    private EmployeeService employeeService;
//    private IFoodWorkerService foodWorkerService;

    private ObservableList<Employee> workers = FXCollections.observableArrayList();
//    private ObservableList<FoodWorker> workers = FXCollections.observableArrayList();



//    private String firstNames;
//    private String lastName;
//    private LocalTime startDate;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("firstNames"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("lastName"));
//        time.setCellValueFactory(new PropertyValueFactory<>("registerDateTime"));
        time.setCellValueFactory(new PropertyValueFactory<>("formattedDateTime"));

        loadWorkers();
    }

    private void loadWorkers() {
        workers.clear();

//        System.out.println("\nAll EMPLOYEE records");
//        for (Employee e : repo.findAll()){
//            System.out.println("- " + e);
//        }
        workers.addAll(repo.findAll());
        foodWorkerTable.setItems(workers);
    }


    @FXML
    private void addWorker() {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Add Employee");
        dialog.setHeaderText("First names:");
        Optional<String> typeResult = dialog.showAndWait();
        if (typeResult.isEmpty()) return;

        dialog.setHeaderText("Last name:");
        Optional<String> specResult = dialog.showAndWait();
//        Optional<String> specResult = dialog.show();
        if (specResult.isEmpty()) return;


        // Worker type dropdown
        List<String> workerTypes = List.of("Maintenance Worker", "Housekeeper", "Food Worker");
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(workerTypes.get(0), workerTypes);
        choiceDialog.setTitle("Select Worker Type");
        choiceDialog.setHeaderText("Choose the type of worker:");
        Optional<String> workerType = choiceDialog.showAndWait();
        if (workerType.isEmpty()) return;

        System.out.println("worker type selected: " + workerType);

        // formatted date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        String formattedDateTime = now.format(formatter);
//        System.out.println("Current Date and Time: " + formattedDateTime);

        Employee employee = new Employee(
                typeResult.get(),
                specResult.get(),
//                currentDateTime,
                formattedDateTime
        );

        String workerTypeStr = workerType.get().trim();

        //save PK to use as FK
        Employee employeePk = employeeService.create(employee);
        System.out.println("Employee ID (FK for 3 other entities): " + employeePk.getId());

        if (workerTypeStr.equalsIgnoreCase("Food Worker")) {
            System.out.println("adding food worker FK");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-foodWorker.fxml"));
                loader.setControllerFactory(AppEmployee.getSpringContext()::getBean);
                Parent root = loader.load();

                FoodWorkerControllerView foodWorkerController = loader.getController();
                foodWorkerController.setEmployee(employeePk);

                Stage stage = new Stage();
                stage.setTitle("Food Worker Management");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (workerTypeStr.equalsIgnoreCase("Housekeeper")) {
            System.out.println("adding housekeeper FK");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-housekeeper-d1.fxml"));
                loader.setControllerFactory(AppEmployee.getSpringContext()::getBean);
                Parent root = loader.load();

                HousekeeperViewController hkController = loader.getController();
                hkController.setEmployee(employeePk);

                Stage stage = new Stage();
                stage.setTitle("Housekeeper Management");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("adding maintenance FK");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/MaintenanceWorker-View.fxml"));
                loader.setControllerFactory(AppEmployee.getSpringContext()::getBean);
                Parent root = loader.load();

                MaintenanceWorkerViewController mwController = loader.getController();
                mwController.setEmployee(employeePk);

                Stage stage = new Stage();
                stage.setTitle("Maintenance Worker Management");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }







        loadWorkers();

    }

    @FXML
    private void updateWorker() {
        Employee selected = foodWorkerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a employee to update.");
            return;
        }

        TextInputDialog typeDialog = new TextInputDialog(selected.getFirstNames());
        typeDialog.setTitle("Update Employee");
        typeDialog.setHeaderText("Update First names:");
        Optional<String> typeResult = typeDialog.showAndWait();
        if (typeResult.isEmpty()) return;

        TextInputDialog specDialog = new TextInputDialog(selected.getLastName());
        specDialog.setTitle("Update Employee");
        specDialog.setHeaderText("Update Last name:");
        Optional<String> specResult = specDialog.showAndWait();
        if (specResult.isEmpty()) return;

        selected.setFirstNames(typeResult.get());
        selected.setLastName(specResult.get());
        repo.save(selected);
        loadWorkers();
    }

    @FXML
    private void deleteWorker() {
        Employee selected = foodWorkerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a worker to delete.");
            return;
        }

        repo.deleteById(selected.getId());
        loadWorkers();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

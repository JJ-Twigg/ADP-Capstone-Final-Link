package com.college.controller;

import com.college.domain.Employee;
import com.college.domain.Reservation;
import com.college.domain.subclasses.FoodWorker;
import com.college.service.IFoodWorkerService;
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

import java.util.Optional;

@Component
public class FoodWorkerControllerView {

    @FXML
    private TableView<FoodWorker> foodWorkerTable;
    @FXML
    private TableColumn<FoodWorker, Integer> colId;
    @FXML
    private TableColumn<FoodWorker, String> colType;
    @FXML
    private TableColumn<FoodWorker, String> colSpecialization;

    @Autowired
    private IFoodWorkerService foodWorkerService;

    private ObservableList<FoodWorker> workers = FXCollections.observableArrayList();

    //will store FK object from employee
    private Employee employee;   // hold the FK reference

    //FK setter from employee
    public void setEmployee(Employee employee) {
        this.employee = employee;
        System.out.println("FK received in FoodWorkerController: " + employee);
    }




    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));

        loadWorkers();
    }

    private void loadWorkers() {
        workers.clear();
        workers.addAll(foodWorkerService.getAll());
        foodWorkerTable.setItems(workers);
    }

    @FXML
    private void addWorker() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Worker");
        dialog.setHeaderText("Enter Worker Type:");
        Optional<String> typeResult = dialog.showAndWait();
        if (typeResult.isEmpty()) return;

        dialog.setHeaderText("Enter Worker Specialization:");
        Optional<String> specResult = dialog.showAndWait();
        if (specResult.isEmpty()) return;


        FoodWorker worker = new FoodWorker.FoodWorkerBuilder()
                .type(typeResult.get())
                .specialization(specResult.get())
                .build();

        // SET FK HERE from employeeId integer setter at top we passed
        //set the EMPLOYEE attribute in domain of this particular object with unique attributes we just made, to the pk passed.
        worker.setEmployee(employee);

        foodWorkerService.create(worker);

        loadWorkers();
    }

    @FXML
    private void updateWorker() {
        FoodWorker selected = foodWorkerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a worker to update.");
            return;
        }

        TextInputDialog typeDialog = new TextInputDialog(selected.getType());
        typeDialog.setTitle("Update Worker");
        typeDialog.setHeaderText("Update Worker Type:");
        Optional<String> typeResult = typeDialog.showAndWait();
        if (typeResult.isEmpty()) return;

        TextInputDialog specDialog = new TextInputDialog(selected.getSpecialization());
        specDialog.setTitle("Update Worker");
        specDialog.setHeaderText("Update Worker Specialization:");
        Optional<String> specResult = specDialog.showAndWait();
        if (specResult.isEmpty()) return;

        selected.setType(typeResult.get());
        selected.setSpecialization(specResult.get());
        foodWorkerService.update(selected);  // make sure your service has an update method
        loadWorkers();
    }

    @FXML
    private void deleteWorker() {
        FoodWorker selected = foodWorkerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a worker to delete.");
            return;
        }

        foodWorkerService.delete(selected.getId()); // delete by ID
        loadWorkers();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

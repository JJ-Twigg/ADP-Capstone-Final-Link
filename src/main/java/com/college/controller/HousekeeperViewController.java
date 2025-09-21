package com.college.controller;

import com.college.domain.Employee;
import com.college.domain.Housekeeper;
import com.college.service.HousekeeperService;
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

import java.util.List;

@Component
public class HousekeeperViewController {

    @FXML private TableView<Housekeeper> housekeeperTable;
    @FXML private TableColumn<Housekeeper, Integer> colId;
    @FXML private TableColumn<Housekeeper, String> colName;
    @FXML private TableColumn<Housekeeper, String> colSurname;

    @Autowired
    private HousekeeperService housekeeperService;

    @Autowired
    private ApplicationContext applicationContext;

    private final ObservableList<Housekeeper> housekeepers = FXCollections.observableArrayList();


    //will store FK object from employee
    private Employee employee;  // hold the FK reference

    //FK setter from employee
    public void setEmployee(Employee employee) {
        this.employee = employee;
        System.out.println("FK received in FoodWorkerController: " + employee.getId());
    }




    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("housekeeperId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("housekeeperName"));
        colSurname.setCellValueFactory(new PropertyValueFactory<>("housekeeperSurname"));
        housekeeperTable.setItems(housekeepers);
        load();
    }

    private void load() {
        try {
            List<Housekeeper> list = housekeeperService.getAll();
            Platform.runLater(() -> {
                housekeepers.clear();
                housekeepers.addAll(list);
            });
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error loading data: " + e.getMessage());
        }
    }

    @FXML
    private void handleAdd() {
        openForm(null);
    }

    @FXML
    private void handleUpdate() {
        Housekeeper selected = housekeeperTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a row.");
            return;
        }
        openForm(selected);
    }

    @FXML
    private void handleDelete() {
        Housekeeper selected = housekeeperTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a row.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete selected housekeeper?", ButtonType.YES, ButtonType.NO);
        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            try {
                housekeeperService.delete(selected.getHousekeeperId());
                load();
                showAlert(Alert.AlertType.INFORMATION, "Deleted.");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Delete failed: " + e.getMessage());
            }
        }
    }

    private void openForm(Housekeeper hk) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dialog_boxes/add-housekeeper.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(hk == null ? "Add Housekeeper" : "Update Housekeeper");
            stage.setScene(new Scene(loader.load()));
            HousekeeperFormController controller = loader.getController();

            //FOREIGN KEY ADD
            controller.setEmployee(this.employee);


            controller.setHousekeeper(hk);
            stage.showAndWait();




            load();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Open form failed: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type, msg, ButtonType.OK);
        alert.showAndWait();
    }
}


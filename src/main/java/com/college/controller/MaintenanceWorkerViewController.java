package com.college.controller;

import com.college.domain.MaintenanceWorker;
import com.college.service.MaintenanceWorkerService;
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
public class MaintenanceWorkerViewController {

    @FXML private TableView<MaintenanceWorker> table;
    @FXML private TableColumn<MaintenanceWorker, Integer> colId;
    @FXML private TableColumn<MaintenanceWorker, Boolean> colExternal;
    @FXML private TableColumn<MaintenanceWorker, String> colCompany;
    @FXML private TableColumn<MaintenanceWorker, String> colType;

    @Autowired
    private MaintenanceWorkerService service;

    @Autowired
    private ApplicationContext applicationContext;

    private final ObservableList<MaintenanceWorker> workers = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("maintenanceId"));
        colExternal.setCellValueFactory(new PropertyValueFactory<>("external"));
        colCompany.setCellValueFactory(new PropertyValueFactory<>("company"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        table.setItems(workers);
        load();
    }

    private void load() {
        List<MaintenanceWorker> list = service.getAll();
        Platform.runLater(() -> {
            workers.clear();
            workers.addAll(list);
        });
    }

    @FXML
    private void handleAdd() { openForm(null); }

    @FXML
    private void handleUpdate() {
        MaintenanceWorker selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) openForm(selected);
    }

    @FXML
    private void handleDelete() {
        MaintenanceWorker selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            service.delete(selected.getMaintenanceId());
            load();
        }
    }

    private void openForm(MaintenanceWorker worker) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Add_MaintenanceWorker.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(worker == null ? "Add Worker" : "Update Worker");
            stage.setScene(new Scene(loader.load()));
            MaintenanceWorkerFormController controller = loader.getController();
            controller.setWorker(worker);
            stage.showAndWait();
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
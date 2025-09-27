package com.college.controller;

import com.college.domain.Employee;
import com.college.domain.Shift;
import com.college.service.ShiftService;
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
import java.time.LocalTime;

@Component
public class ShiftViewController {

    @FXML private TableView<Shift> shiftTable;
    @FXML private TableColumn<Shift, Integer> colId;
    @FXML private TableColumn<Shift, LocalDate> colDate;
    @FXML private TableColumn<Shift, LocalTime> colStartTime;
    @FXML private TableColumn<Shift, LocalTime> colEndTime;
    @FXML private TableColumn<Shift, Boolean> colOvertime;

    @Autowired
    private ShiftService ShiftService;

    @Autowired
    private ApplicationContext applicationContext;

    private ObservableList<Shift> shifts = FXCollections.observableArrayList();



    //will store FK object from employee
    private Employee employee;  // hold the FK reference

    //FK setter from employee
    public void setEmployee(Employee employee) {
        this.employee = employee;

    }


    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("shiftId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("shiftDay"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("shiftStartTime"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<>("shiftEndTime"));
        colOvertime.setCellValueFactory(new PropertyValueFactory<>("shiftOvertime"));
        shiftTable.setItems(shifts);

        // Debug: Check if Spring injection worked
        System.out.println("ShiftViewController initialized");
        System.out.println("ShiftService injected: " + (ShiftService != null));
        System.out.println("ApplicationContext injected: " + (applicationContext != null));

        loadShifts();
    }

    private void loadShifts() {
        try {
            List<Shift> list = ShiftService.getAll();
            Platform.runLater(() -> {
                shifts.clear();
                shifts.addAll(list);
            });
        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> showAlert("Error loading shifts: " + e.getMessage()));
        }
    }

    @FXML
    private void handleAddShift() {
        openForm(null);
    }

    @FXML
    private void handleUpdateShift() {
        Shift selected = shiftTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openForm(selected);
        } else {
            showAlert("Please select a shift to update.");
        }
    }

    @FXML
    private void handleDeleteShift() {
        Shift selected = shiftTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete this shift?",
                    ButtonType.YES, ButtonType.NO);

            if (confirmAlert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                try {
                    ShiftService.delete(selected.getShiftId());
                    showAlert(Alert.AlertType.INFORMATION, "Shift deleted successfully!");
                    loadShifts();
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Error deleting shift: " + e.getMessage());
                }
            }
        } else {
            showAlert("Please select a shift to delete.");
        }
    }

    private void openForm(Shift shift) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Add_Shift.fxml"));

            // This is the key fix: Use Spring to create the controller
            loader.setControllerFactory(applicationContext::getBean);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(shift == null ? "Add Shift" : "Update Shift");
            stage.setScene(new Scene(loader.load()));

            ShiftFormController controller = loader.getController();
            controller.setShift(shift);

            stage.showAndWait();
            loadShifts(); // Refresh the table
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

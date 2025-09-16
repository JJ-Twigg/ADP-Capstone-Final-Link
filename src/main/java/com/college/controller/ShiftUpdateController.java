package com.college.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.time.LocalTime;
import java.time.LocalDate;
import com.college.domain.Shift;
import com.college.service.ShiftService;

@Controller
public class ShiftUpdateController {

    @FXML
    private DatePicker datePicker;
    @FXML
    private Spinner<Integer> spinnerStartHour;
    @FXML
    private Spinner<Integer> spinnerStartMinute;
    @FXML
    private Spinner<Integer> spinnerEndHour;
    @FXML
    private Spinner<Integer> spinnerEndMinute;
    @FXML
    private CheckBox chkOvertime;
    @FXML
    private Button cancelButton; // Add this field

    @Autowired
    private ShiftService shiftService;

    private Shift shift;

    @FXML
    public void initialize() {
        // Default date = today
        datePicker.setValue(LocalDate.now());

        spinnerStartHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 8));
        spinnerStartMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        spinnerEndHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 17));
        spinnerEndMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));

        // Debug: confirm controller loaded
        System.out.println("ShiftUpdateController initialized"); // Fixed name
        System.out.println("ShiftService injected: " + (shiftService != null));
    }

    public void setShift(Shift shift) {
        this.shift = shift;
        if (shift != null) {
            // Populate form with existing shift
            datePicker.setValue(shift.getShiftDay());
            spinnerStartHour.getValueFactory().setValue(shift.getShiftStartTime().getHour());
            spinnerStartMinute.getValueFactory().setValue(shift.getShiftStartTime().getMinute());
            spinnerEndHour.getValueFactory().setValue(shift.getShiftEndTime().getHour());
            spinnerEndMinute.getValueFactory().setValue(shift.getShiftEndTime().getMinute());
            chkOvertime.setSelected(shift.getShiftOvertime());
        }
    }

    @FXML // Add this annotation if called from FXML
    private void updateShift() {
        try {
            if (datePicker.getValue() == null) {
                showAlert("Please select a shift date");
                return;
            }

            LocalDate date = datePicker.getValue();
            LocalTime start = LocalTime.of(spinnerStartHour.getValue(), spinnerStartMinute.getValue());
            LocalTime end = LocalTime.of(spinnerEndHour.getValue(), spinnerEndMinute.getValue());
            boolean overtime = chkOvertime.isSelected();

            // Update existing shift
            shift.setShiftDay(date);
            shift.setShiftStartTime(start);
            shift.setShiftEndTime(end);
            shift.setShiftOvertime(overtime);

            shiftService.update(shift);
            showAlert(Alert.AlertType.INFORMATION, "Shift updated successfully!");

            closeWindow();

        } catch (Exception ex) {
            showAlert("Error: " + ex.getMessage());
        }
    }

    @FXML
    private void closeWindow() {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    private void showAlert(String message) {
        showAlert(Alert.AlertType.WARNING, message);
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.showAndWait();
    }
} // Make sure all methods are inside the class

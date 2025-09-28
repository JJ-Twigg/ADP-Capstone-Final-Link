package com.college.controller;

import com.college.domain.Employee;
import com.college.domain.Shift;
import com.college.service.EmployeeService;
import com.college.service.ShiftService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class ShiftFormController {

    @FXML private DatePicker datePicker;   // Shift date
    @FXML private Spinner<Integer> spinnerStartHour;
    @FXML private Spinner<Integer> spinnerStartMinute;
    @FXML private Spinner<Integer> spinnerEndHour;
    @FXML private Spinner<Integer> spinnerEndMinute;
    @FXML private CheckBox chkOvertime;

    @Autowired
    private ShiftService shiftService;

    @Autowired
    EmployeeService empService;

    private Shift shift;


    //WHICH EMPLOYEE TO ADD SHIFT TO Combo Box
    @FXML
    private ComboBox<Employee> employeeComboBox;



    //will store FK object from employee
    private Employee employee;  // hold the FK reference

    //FK setter from employee
    public void setEmployee(Employee employee) {
        this.employee = employee;
//        System.out.println("FK received in FoodWorkerController: " + employee.getId());
    }






    @FXML
    public void initialize() {
        // Default date = today
        datePicker.setValue(LocalDate.now());

        // Configure spinners (24-hour format)
        spinnerStartHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 8));
        spinnerStartMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        spinnerEndHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 17));
        spinnerEndMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));

        // Debug: confirm controller loaded
        System.out.println("ShiftFormController initialized");
        System.out.println("ShiftService injected: " + (shiftService != null));

        try {
            List<Employee> employees = empService.getAllEmployees(); // fetch all employees
            employeeComboBox.setItems(FXCollections.observableArrayList(employees));

            // Display employee names in ComboBox
            employeeComboBox.setCellFactory(cb -> new ListCell<>() {
                @Override
                protected void updateItem(Employee emp, boolean empty) {
                    super.updateItem(emp, empty);
                    setText(empty || emp == null ? "" : emp.getUser().getName() + " " + emp.getUser().getSurname());
                }
            });

            employeeComboBox.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Employee emp, boolean empty) {
                    super.updateItem(emp, empty);
                    setText(empty || emp == null ? "" : emp.getUser().getName() + " " + emp.getUser().getSurname());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading employees into ComboBox: " + e.getMessage());
        }

    }

    public void setShift(Shift shift) {
        this.shift = shift;
        if (shift != null) {
            // Populate form with existing shift
            datePicker.setValue(shift.getShiftDay());   // entity should use LocalDate
            spinnerStartHour.getValueFactory().setValue(shift.getShiftStartTime().getHour());
            spinnerStartMinute.getValueFactory().setValue(shift.getShiftStartTime().getMinute());
            spinnerEndHour.getValueFactory().setValue(shift.getShiftEndTime().getHour());
            spinnerEndMinute.getValueFactory().setValue(shift.getShiftEndTime().getMinute());
            chkOvertime.setSelected(shift.getShiftOvertime());
        }
    }

    @FXML
    private void handleSave() {
        try {
            if (datePicker.getValue() == null) {
                showAlert("Please select a shift date");
                return;
            }

            LocalDate date = datePicker.getValue();
            LocalTime start = LocalTime.of(spinnerStartHour.getValue(), spinnerStartMinute.getValue());
            LocalTime end = LocalTime.of(spinnerEndHour.getValue(), spinnerEndMinute.getValue());
            boolean overtime = chkOvertime.isSelected();

            if (shift == null) {
                // Create new shift
                Shift newShift = new Shift.Builder()
                        .setShiftDay(date)
                        .setShiftStartTime(start)
                        .setShiftEndTime(end)
                        .setShiftOvertime(overtime)
                        .build();

                Employee selectedEmployee = employeeComboBox.getSelectionModel().getSelectedItem();
                if (selectedEmployee == null) {
                    showAlert("Please select an employee");
                    return;
                }


                //HERE U CHECK: if the employee object, has a shift assigned to it. getShift of employee called. it returns shift object jpa
                if (selectedEmployee.getShift() != null) {
                    showAlert("This employee already has a shift assigned!");
                    return;
                }

                newShift.setEmployee(selectedEmployee);


                shiftService.create(newShift);



                showAlert(Alert.AlertType.INFORMATION, "Shift created successfully!");
            } else {
                // Update existing shift
                shift.setShiftDay(date);
                shift.setShiftStartTime(start);
                shift.setShiftEndTime(end);
                shift.setShiftOvertime(overtime);

                shiftService.update(shift);
                showAlert(Alert.AlertType.INFORMATION, "Shift updated successfully!");
            }

            closeWindow();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error saving shift: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) datePicker.getScene().getWindow()).close();
    }

    private void showAlert(String message) {
        showAlert(Alert.AlertType.WARNING, message);
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.showAndWait();
    }
}

package com.college.controller;

import com.college.domain.EmployeeSalary;
import com.college.service.EmployeeSalaryService;
import com.college.service.IEmployeeSalaryService;
import com.college.service.IEmployeeService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.application.Platform;




import java.util.List;

@RestController
@RequestMapping("/employeeSalary")
public class EmployeeSalaryController {

    private final IEmployeeSalaryService service;

    @Autowired
    public EmployeeSalaryController(IEmployeeSalaryService employeeSalaryService) {
        this.service = employeeSalaryService;
    }

//    @GetMapping("/get/{id}")
//    public EmployeeSalary read(@PathVariable int id) {
//        return service.findById(id).orElse(null);
//    }
//
//    @GetMapping("/get/all")
//    public List<EmployeeSalary> getAll() {
//        return service.getAll();
//    }
//
//    @PostMapping("/create")
//    public EmployeeSalary create(@RequestBody EmployeeSalary employeeSalary) {
//        return service.create(employeeSalary);
//    }
//
//    @PutMapping("/update")
//    public EmployeeSalary update(@RequestBody EmployeeSalary employeeSalary) {
//        return service.update(employeeSalary);
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public boolean delete(@PathVariable int id) {
//        service.delete(id);
//        return true;
//    }


    class EmployeeSalaryViewController {

        @FXML private TableView<EmployeeSalary> employeeSalaryTable;
        @FXML private TableColumn<EmployeeSalary, Integer> colId;
        @FXML private TableColumn<EmployeeSalary, Double> colAmount;
        @FXML private TableColumn<EmployeeSalary, String> colMethod;
        @FXML private TableColumn<EmployeeSalary, LocalDate> colDate;

        @Autowired
        private ApplicationContext applicationContext;
        private EmployeeSalaryService employeeSalaryService;

        private ObservableList<EmployeeSalary> employeeSalaries = FXCollections.observableArrayList();

        @FXML
        public void initialize() {
            colId.setCellValueFactory(new PropertyValueFactory<>("salaryId"));
            colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
            colMethod.setCellValueFactory(new PropertyValueFactory<>("method"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            employeeSalaryTable.setItems(employeeSalaries);

            // Debug: Check if Spring injection worked
            System.out.println("EmployeeSalaryViewController initialized");
            System.out.println("EmployeeSalaryService injected: " + (employeeSalaryService != null));

            loadEmployeeSalaries();
        }

        private void loadEmployeeSalaries() {
            try {
                List<EmployeeSalary> list = employeeSalaryService.getAll();
                Platform.runLater (() -> { Platform.runLater (() -> {
                    employeeSalaries.clear();
                    employeeSalaries.addAll(list);
                });
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> showAlert("Error loading employee salaries: " + e.getMessage()));

                }
            }

            @FXML
        private void handleAddEmployeeSalary() {
            openForm(null);
        }

        @FXML
        private void handleUpdateEmployeeSalary() {
            EmployeeSalary selected = employeeSalaryTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                openForm(selected);
            } else {
                showAlert("Please select an employee salary to update");
            }
        }


        @FXML
        private void handleDeleteEmployeeSalary(){
            EmployeeSalary selected = employeeSalaryTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    employeeSalaryService.delete(selected.getSalaryId());
                    showAlert(Alert.AlertType.INFORMATION, "Employee salary deleted successfully");
                    loadEmployeeSalaries();
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Error deleting employee salary: " + e.getMessage());
                }
            } else {
                showAlert("Please select an employee salary to delete");
            }
        }

        private void openForm(EmployeeSalary employeeSalary) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Add_EmployeeSalary.fxml"));
              loader.setControllerFactory(applicationContext::getBean);

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle(employeeSalary == null ? "Add Employee Salary" : "Update Employee Salary");
                stage.setScene(new Scene(loader.load()));

                EmployeeSalaryFormController controller = loader.getController();
                controller.setEmployeeSalary(employeeSalary);

                stage.showAndWait();
                loadEmployeeSalaries();
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

class EmployeeSalaryFormController {

        @FXML private TextField txtAmount;
        @FXML private ChoiceBox<String> choiceMethod;
        @FXML private DatePicker datePicker;

        @Autowired
     private EmployeeSalaryService employeeSalaryService;

        private EmployeeSalary employeeSalary;

        @FXML
        public void initialize() {
            choiceMethod.getItems().addAll("Cash", "Card", "EFT");
            datePicker.setValue(LocalDate.now());

            System.out.println("EmployeeSalaryFormController initialized");
            System.out.println("EmployeeSalaryService injected: " + (employeeSalaryService != null));
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
                if (txtAmount.getText(). trim().isEmpty()) {
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
            }
            catch (NumberFormatException e){
                showAlert("Invalid amount format");
            }
            catch (Exception e) {
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


 class EmployeeSalaryUpdateController{

        @FXML private TextField txtAmount;
        @FXML private ChoiceBox<String> choiceMethod;
        @FXML private DatePicker datePicker;
        @FXML private Button cancelButton;

        @Autowired
        private EmployeeSalaryService employeeSalaryService;
        private EmployeeSalary employeeSalary;

        @FXML
    public void initialize(){
            choiceMethod.getItems().addAll("Cash", "Card", "EFT");
            datePicker.setValue(LocalDate.now());

        }

        public void setEmployeeSalary(EmployeeSalary employeeSalary){
            this.employeeSalary = employeeSalary;
            if (employeeSalary != null){
                txtAmount.setText(String.valueOf(employeeSalary.getAmount()));
                choiceMethod.setValue(employeeSalary.getMethod());
                datePicker.setValue(employeeSalary.getDate());
            }
        }

        @FXML
    private void updateEmployeeSalary(){
            if (employeeSalary != null){
                try {
                    double amount = Double.parseDouble(txtAmount.getText().trim());
                    String method = choiceMethod.getValue();
                    LocalDate date = datePicker.getValue();

                    if (method == null || date == null){
                        showAlert("All fields are required!");
                        return;
                    }

                    employeeSalary.setAmount(amount);
                    employeeSalary.setMethod(method);
                    employeeSalary.setDate(date);

                    employeeSalaryService.update(employeeSalary);
                    showAlert(Alert.AlertType.INFORMATION, "Employee salary updated successfully");
                    closeWindow();
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Error: " + e.getMessage());
                }
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
 }


}

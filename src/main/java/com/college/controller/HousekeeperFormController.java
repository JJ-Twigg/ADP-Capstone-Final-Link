package com.college.controller;

import com.college.domain.Employee;
import com.college.domain.Housekeeper;
import com.college.service.HousekeeperService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HousekeeperFormController {

    @FXML private TextField txtName;
    @FXML private TextField txtSurname;


    //will store FK object from employee
    private Employee employee;  // hold the FK reference

    //FK setter from employee
    public void setEmployee(Employee employee) {
        this.employee = employee;
        System.out.println("FK received in FoodWorkerController: " + employee.getId());
    }






    @Autowired
    private HousekeeperService housekeeperService;

    private Housekeeper housekeeper;

    public void setHousekeeper(Housekeeper housekeeper) {
        this.housekeeper = housekeeper;
        if (housekeeper != null) {
            txtName.setText(housekeeper.getHousekeeperName());
            txtSurname.setText(housekeeper.getHousekeeperSurname());
        }
    }

    @FXML
    private void handleSave() {
        String name = txtName.getText() == null ? "" : txtName.getText().trim();
        String surname = txtSurname.getText() == null ? "" : txtSurname.getText().trim();

        if (name.isEmpty() || surname.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "All fields are required.");
            return;
        }

        if (housekeeper == null) {
            Housekeeper hk = new Housekeeper.HousekeeperBuilder()
                    .setHousekeeperName(name)
                    .setHousekeeperSurname(surname)
                    .build();

            //setFK to be saved
            hk.setEmployee(employee);

            housekeeperService.create(hk);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Housekeeper created.");
        } else {
            Housekeeper updated = new Housekeeper.HousekeeperBuilder()
                    .setHousekeeperId(housekeeper.getHousekeeperId())
                    .setHousekeeperName(name)
                    .setHousekeeperSurname(surname)
                    .build();
            housekeeperService.update(updated);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Housekeeper updated.");
        }
        close();
    }

    @FXML
    private void handleCancel() { close(); }

    private void close() {
        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type, msg, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}

package com.college.controller;

import com.college.domain.Employee;
import com.college.domain.MaintenanceWorker;
import com.college.service.MaintenanceWorkerService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceWorkerFormController {

    @FXML private CheckBox chkExternal;
    @FXML private TextField txtCompany;
    @FXML private TextField txtType;

    @Autowired
    private MaintenanceWorkerService service;

    private MaintenanceWorker worker;





    //will store FK object from employee
    private Employee employee;  // hold the FK reference

    //FK setter from employee
    public void setEmployee(Employee employee) {
        this.employee = employee;

    }





    public void setWorker(MaintenanceWorker worker) {
        this.worker = worker;
        if (worker != null) {
            chkExternal.setSelected(worker.isExternal());
            txtCompany.setText(worker.getCompany());
            txtType.setText(worker.getType());
        }
    }

    @FXML
    private void handleSave() {
        String company = txtCompany.getText().trim();
        String type = txtType.getText().trim();

        if (company.isEmpty() || type.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation");
            return;
        }

        if (worker == null) {
            MaintenanceWorker newWorker = new MaintenanceWorker.MaintenanceWorkerBuilder()
                    .setExternal(chkExternal.isSelected())
                    .setCompany(company)
                    .setType(type)
                    .build();

            //setFK to be saved
            newWorker.setEmployee(employee);

            service.create(newWorker);
        } else {
            MaintenanceWorker updated = new MaintenanceWorker.MaintenanceWorkerBuilder()
                    .setMaintenanceId(worker.getMaintenanceId())
                    .setExternal(chkExternal.isSelected())
                    .setCompany(company)
                    .setType(type)
                    .build();
            service.update(updated);
        }
        close();
    }

    @FXML
    private void handleCancel() { close(); }

    private void close() {
        Stage stage = (Stage) txtCompany.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String msg) {
        new Alert(type, msg, ButtonType.OK).showAndWait();
    }
}

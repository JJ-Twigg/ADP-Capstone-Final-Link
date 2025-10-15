package com.college.controller;

import com.college.MainFinal;
import com.college.domain.Employee;
import com.college.domain.Role;
import com.college.domain.User;
import com.college.domain.UserRole;
import com.college.service.EmployeeService;
import com.college.service.RoleService;
import com.college.service.UserRoleService;
import com.college.service.UserService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;

@Component
public class UserRegisterController {

    @FXML private TextField nameField;
    @FXML private TextField surnameField;
    @FXML private TextField ageField;
    @FXML private TextField genderField;

    @FXML private TextField username;
    @FXML private PasswordField password;

    @Autowired private UserService userService;

    @FXML private ComboBox<String> roleComboBox;
    @FXML private ComboBox<String> otherRoleComboBox; // new secondary ComboBox

    @Autowired
    EmployeeService employeeService;

    @Autowired
    RoleService roleService;



    @Autowired
    private UserRoleService userRoleService;

    @FXML
    public void initialize() {
        // Populate main role ComboBox
        roleComboBox.getItems().addAll("ADMIN", "MANAGER", "USER");

        // Populate secondary role ComboBox
        otherRoleComboBox.getItems().addAll("FoodWorker", "MaintenanceWorker", "Housekeeper");
        otherRoleComboBox.setVisible(false);

        // Show/hide secondary combo based on selection
        roleComboBox.setOnAction(event -> {
            String selected = roleComboBox.getSelectionModel().getSelectedItem();
            if ("USER".equals(selected)) {
                otherRoleComboBox.setVisible(true);
            } else {
                otherRoleComboBox.setVisible(false);
                otherRoleComboBox.getSelectionModel().clearSelection();
            }
        });

        // Prevent maximize
        Platform.runLater(() -> {
            Stage stage = (Stage) username.getScene().getWindow();
            stage.maximizedProperty().addListener((obs, wasMaximized, isNowMaximized) -> {
                if (isNowMaximized) {
                    stage.setMaximized(false);
                }
            });
        });

    }


    // Signup button click
    @FXML
    private void handleSignUp(ActionEvent event) {
        String email = username.getText().trim();
        String pass = password.getText();

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(emailRegex)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Email");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid email address!");
            alert.showAndWait();
            return; // stop signup
        }

        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String ageText = ageField.getText().trim();
        String gender = genderField.getText().trim();
        String role = roleComboBox.getValue();

        //type of normal user, FW, Housekeeper etc.
        String userType;

        // if user selected, define type of user the person will be
        if ("USER".equals(role) && otherRoleComboBox.getValue() != null) {
            userType = otherRoleComboBox.getValue();
        }

        if (email.isEmpty() || pass.isEmpty() || name.isEmpty() || surname.isEmpty()
                || ageText.isEmpty() || gender.isEmpty() || role == null) {
            System.out.println("All fields are required!");
            return;
        }

        Integer age;

        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Age");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid numeric age!");
            alert.showAndWait();
            return;
        }

        try {
            User user = userService.register(email, pass, name, surname, age, gender, role);
            System.out.println("User registered successfully!");

            String jobType;
            if ("USER".equals(role)) {
                jobType = otherRoleComboBox.getValue(); // Housekeeper, Maintenance, FoodWorker
                if (jobType == null) {
                    System.out.println("Please select a user type!");
                    return;
                }
            } else {
                jobType = role; // ADMIN or MANAGER
            }

            //PASS FK AS USER OBJECT, JPA AUTO uses user object and gives fk from it to employee
            Employee emp = new Employee(jobType, LocalDate.now(), user);

            employeeService.createEmployee(emp);

            //  Handle Role entity, user and role must be made first, bridge uses both their fk's
                Role roleEntity = new Role.RoleBuilder().roleName(role).build();
                roleService.create(roleEntity);

            String specification;
            switch (jobType) {
                case "MANAGER":
                    specification = "crudEmployees";
                    break;
                case "ADMIN":
                    specification = "crudEmployeesAndManagers";
                    break;
                case "Housekeeper":
                case "MaintenanceWorker":
                case "FoodWorker":
                    specification = "viewDetailsAndShift";
                    break;
                default:
                    specification = "Null!"; // fallback
                    break;
            }




            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(roleEntity);

            userRole.setSpecification(specification);


            userRoleService.create(userRole);




            // 1. save Users credentials
            // 2. immediately save employee with those credentials
            // 3. insert into bridge and role table immediately also
            // 4. only then call login page


            goToLoginPage(event);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @FXML
    private void goToLoginPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/windowLoginFinal.fxml"));
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean);

            Parent loginPage = loader.load();
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(loginPage);

            // Optionally add CSS
            String stylesheet = getClass().getResource("/css/buttonStyle.css").toExternalForm();
            if (!scene.getStylesheets().contains(stylesheet)) {
                scene.getStylesheets().add(stylesheet);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/windowLoginFinal.fxml"));
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean);

            Parent loginPage = loader.load();
            Stage stage = (Stage) username.getScene().getWindow();
            stage.getScene().setRoot(loginPage);

            String stylesheet = getClass().getResource("/css/buttonStyle.css").toExternalForm();
            Scene scene = stage.getScene();
            if (!scene.getStylesheets().contains(stylesheet)) {
                scene.getStylesheets().add(stylesheet);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}

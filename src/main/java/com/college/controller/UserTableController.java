package com.college.controller;

import com.college.domain.Role;
import com.college.domain.User;
import com.college.service.RoleService;
import com.college.service.UserService;
import javafx.beans.property.SimpleStringProperty;
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

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
public class UserTableController {

    @FXML
    private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> userIdColumn;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> surnameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> roleColumn;
    @FXML private TableColumn<User, String> startDateColumn;
    @FXML private TableColumn<User, Integer> ageColumn;
    @FXML private TableColumn<User, String> genderColumn;

    @Autowired
    private UserService userService;

    @Autowired
    RoleService roleService;

    private final ObservableList<User> userList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        // Bind table columns
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

        // Format startDate as string
        startDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getStartDate() != null
                        ? cellData.getValue().getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        : ""
        ));

        // Load data from DB
        loadUsers();
    }

    private void loadUsers() {
        try {
            List<User> users = userService.getAll();
            userList.setAll(users);
            userTable.setItems(userList);
            System.out.println("Loaded " + users.size() + " users.");
        } catch (Exception e) {
            System.out.println("Failed to load users: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void addUser() {
        System.out.println("Add User clicked - implement form here");
    }








    @FXML
    private void updateEmployee() {
//        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select an employee to update.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selected.getName());
        dialog.setTitle("Update Name");
        dialog.setHeaderText("Update Name");
        Optional<String> name = dialog.showAndWait();
        if (name.isEmpty()) return;

        TextInputDialog dialog1 = new TextInputDialog(selected.getSurname());
        dialog1.setTitle("Update Surname");
        dialog1.setHeaderText("Update Surname");
        Optional<String> surname = dialog1.showAndWait();
        if (surname.isEmpty()) return;

        TextInputDialog dialog2 = new TextInputDialog(selected.getName());
        dialog2.setTitle("Update Email address");
        dialog2.setHeaderText("Update Email address");
        Optional<String> email = dialog2.showAndWait();
        if (email.isEmpty()) return;
//
//        dialog.setHeaderText("Update Start Date (yyyy-MM-dd):");
//        Optional<String> startDateResult = dialog.showAndWait();
//        if (startDateResult.isEmpty()) return;


        // updating details //
//        selected.setJobType(jobTypeResult.get());
        selected.setName(name.get());
        selected.setSurname(surname.get());
        selected.setEmail(email.get());
//        selected.setStartDate(LocalDate.parse(startDateResult.get()));

        userService.create(selected);
//        repo.save(selected);
        loadUsers();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

//    @FXML
//    public void updateUser() {
//        User selected = userTable.getSelectionModel().getSelectedItem();
//        if (selected != null) {
//            System.out.println("Update User: " + selected.getName());
//        } else {
//            System.out.println("Select a user to update.");
//        }
//    }

    @FXML
    public void deleteUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                boolean deleted = userService.delete(selected.getUserId());

                if (deleted) {
                    // Clean up orphaned roles
                    List<Role> allRoles = roleService.getAll();
                    for (Role role : allRoles) {
                        boolean used = roleService.isRoleUsed(role.getId());
                        if (!used) {
                            roleService.delete(role.getId());
                        }
                    }

                    userList.remove(selected);
                    System.out.println("Deleted user: " + selected.getName() + " and cleaned up unused roles.");
                } else {
                    System.out.println("Failed to delete user.");
                }

            } catch (Exception e) {
                System.out.println("Error deleting user: " + e.getMessage());
                e.printStackTrace();
            }

        } else {
            System.out.println("Select a user to delete.");
        }
    }

}

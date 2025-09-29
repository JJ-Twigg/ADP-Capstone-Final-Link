package com.college.controller;

import com.college.domain.Role;
import com.college.domain.User;
import com.college.service.RoleService;
import com.college.service.UserService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

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

    @FXML
    private Label labelFeedback;

    @Autowired
    private UserService userService;

    @Autowired
    RoleService roleService;

    private final ObservableList<User> userList = FXCollections.observableArrayList();

    // ✅ This method is automatically called by JavaFX when FXML is loaded
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
            labelFeedback.setText("Loaded " + users.size() + " users.");
        } catch (Exception e) {
            labelFeedback.setText("Failed to load users: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void addUser() {
        labelFeedback.setText("Add User clicked - implement form here");
    }

    @FXML
    public void updateUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            labelFeedback.setText("Update User: " + selected.getName());
        } else {
            labelFeedback.setText("Select a user to update.");
        }
    }

    @FXML
    public void deleteUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                // Delete the user , cascade deletes UserRoles and Employee Auto
                boolean deleted = userService.delete(selected.getUserId());

                //cascade handles shift and employeeSalary

                if (deleted) {

                    // Clean up orphaned roles ,roles with no UserRoles left
                    List<Role> allRoles = roleService.getAll(); // fetch all roles
                    for (Role role : allRoles) {
                        boolean used = roleService.isRoleUsed(role.getId()); // check if any UserRole still references it
                        if (!used) {
                            roleService.delete(role.getId());
                        }
                    }

                    // Update table and feedback
                    userList.remove(selected);
                    labelFeedback.setText("Deleted user: " + selected.getName() + " and cleaned up unused roles.");

                } else {
                    labelFeedback.setText("Failed to delete user.");
                }

            } catch (Exception e) {
                labelFeedback.setText("Error deleting user: " + e.getMessage());
                e.printStackTrace();
            }

        } else {
            labelFeedback.setText("Select a user to delete.");
        }
    }

}

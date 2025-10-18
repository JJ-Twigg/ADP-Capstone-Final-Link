package com.college.controller;

import com.college.MainFinal;
import com.college.domain.Guest;
import com.college.repository.GuestRepository;
import com.college.service.GuestUIServiceNaked;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GuestUIController {

    @FXML
    private TableView<Guest> guestTable;

    @FXML
    private TableColumn<Guest, Integer> colGuestID;
    @FXML
    private TableColumn<Guest, String> colName;
    @FXML
    private TableColumn<Guest, String> colSurname;
    @FXML
    private TableColumn<Guest, String> colEmail;
    @FXML
    private TableColumn<Guest, String> colContact;
    @FXML
    private TableColumn<Guest, String> colPayment;

    @FXML
    private TextField searchNameField;

    @FXML
    private TextField searchSurnameField;

    private final GuestUIServiceNaked guestService;
    private final GuestRepository guestRepository;



    @Autowired
    public GuestUIController(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
        this.guestService = new GuestUIServiceNaked(guestRepository);
    }

    @FXML
    public void initialize() {

        colGuestID.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getGuestID()).asObject());
        colName.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));   // Amount → Name
        colSurname.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getSurname())); // Method → Surname
        colEmail.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));   // Status → Email
        colContact.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getContactNumber())); // Date → Contact
        colPayment.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getPaymentDetails()));

        guestTable.setFixedCellSize(35);
        guestTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        loadGuests();
    }













    private void loadGuests() {
        try {
            List<Guest> guests = guestService.getAllGuests();
            guestTable.setItems(FXCollections.observableArrayList(guests));
        } catch (Exception e) {
            showAlert("Error", "Failed to load guests: " + e.getMessage());
        }
    }
















    @FXML
    private void handleAddGuest() {
        AddEditGuestDialog dialog = new AddEditGuestDialog(guestRepository, null);

        // Show dialog and handle the result
        dialog.showAndWait().ifPresent(guest -> {
            try {



                // SAVE GUEST FIRST
                Guest savedGuest = guestService.addGuest(guest);

                // Refresh guest table
                loadGuests();

                // THEN open reservation page
                openReservationPage(savedGuest);

            } catch (Exception e) {
                showAlert("Error", "Failed to add guest: " + e.getMessage());
            }
        });
    }

    private void openReservationPage(Guest guest) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/reservationFinal.fxml"));
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean);

            Parent root = loader.load();

            // Create a new stage for the Reservation page
            Stage reservationStage = new Stage();
            reservationStage.setTitle("Reservation");
            reservationStage.setScene(new Scene(root));
            reservationStage.setWidth(1000);
            reservationStage.setHeight(600);

            // Pass guest AND stage to the controller
            ReservationUIController controller = loader.getController();
            controller.setGuest(guest);
            controller.setStage(reservationStage);  // <- important!

            // Show reservation page as non-blocking window
            reservationStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open Reservation page: " + e.getMessage());
        }
    }
















    @FXML
    private void handleEditGuest() {
        Guest selected = guestTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Info", "Please select a guest to edit.");
            return;
        }

        Dialog<Guest> dialog = new AddEditGuestDialog(guestRepository, selected); // pass both repo and guest
        dialog.showAndWait().ifPresent(guest -> {
            try {
                guestService.updateGuest(guest);
                loadGuests();
            } catch (Exception e) {
                showAlert("Error", "Failed to update guest: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleDeleteGuest() {
        Guest selected = guestTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Info", "Please select a guest to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Guest");
        alert.setHeaderText("Delete guest ID: " + selected.getGuestID() + "?");
        alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
            try {
                guestService.deleteGuest(selected.getGuestID());
                loadGuests();
            } catch (Exception e) {
                showAlert("Error", "Failed to delete guest: " + e.getMessage());
            }
        });
    }













    @FXML
    private void handleOpenEventScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-event.fxml"));
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean); // use Spring context
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Event Screen");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open Event screen: " + e.getMessage());
        }
    }


    @FXML
    public void handleSearchGuest() {
        String name = searchNameField.getText().trim();
        String surname = searchSurnameField.getText().trim();

        if (name.isEmpty() || surname.isEmpty()) {
            showAlert("Error", "Please enter both name and surname.");
            return;
        }

        System.out.println("Searching for guest: " + name + " " + surname);

        // Get all guests matching the name and surname
        List<Guest> guests = guestRepository.findByNameAndSurname(name, surname);

        if (guests.isEmpty()) {
            guestTable.getItems().clear(); // clear table if not found
            showAlert("Not Found", "No guest found with that name and surname.");
        } else {
            guestTable.setItems(FXCollections.observableArrayList(guests));
            if (guests.size() == 1) {
                showAlert("Guest Found", "Guest discovered successfully.");
            } else {
                showAlert("Multiple Guests Found", guests.size() + " guests found with that name and surname.");
            }
        }
    }










    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

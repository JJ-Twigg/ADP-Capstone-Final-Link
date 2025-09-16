////package com.college.controller;
////
////import com.college.domain.Guest;
////import com.college.service.GuestService;
////import com.college.service.GuestUIService;
////import javafx.collections.FXCollections;
////import javafx.fxml.FXML;
////import javafx.scene.control.*;
////
////import java.util.List;
////
////public class GuestUIController {
////
////    @FXML
////    private TableView<Guest> guestTable;
////
////    @FXML
////    private TableColumn<Guest, Integer> colGuestID;
////    @FXML
////    private TableColumn<Guest, String> colName;
////    @FXML
////    private TableColumn<Guest, String> colSurname;
////    @FXML
////    private TableColumn<Guest, String> colEmail;
////    @FXML
////    private TableColumn<Guest, String> colContact;
////    @FXML
////    private TableColumn<Guest, String> colPayment;
////
////
////    private final GuestUIService guestService = new GuestUIService();
////
////    @FXML
////    public void initialize() {
////        colGuestID.setCellValueFactory(data ->
////                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getGuestID()).asObject());
////        colName.setCellValueFactory(data ->
////                new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
////        colSurname.setCellValueFactory(data ->
////                new javafx.beans.property.SimpleStringProperty(data.getValue().getSurname()));
////        colEmail.setCellValueFactory(data ->
////                new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
////        colContact.setCellValueFactory(data ->
////                new javafx.beans.property.SimpleStringProperty(data.getValue().getContactNumber()));
////        colPayment.setCellValueFactory(data ->
////                new javafx.beans.property.SimpleStringProperty(data.getValue().getPaymentDetails()));
////
////
////        loadGuests();
////    }
////
////    private void loadGuests() {
////        try {
////            List<Guest> guests = guestService.getAllGuests();
////            guestTable.setItems(FXCollections.observableArrayList(guests));
////        } catch (Exception e) {
////            showAlert("Error", "Failed to load guests: " + e.getMessage());
////        }
////    }
////
////    @FXML
////    private void handleAddGuest() {
////        Dialog<Guest> dialog = new AddEditGuestDialog(null);
////        dialog.showAndWait().ifPresent(guest -> {
////            try {
////                guestService.addGuest(guest);
////                loadGuests();
////            } catch (Exception e) {
////                showAlert("Error", "Failed to add guest: " + e.getMessage());
////            }
////        });
////    }
////
////    @FXML
////    private void handleEditGuest() {
////        Guest selected = guestTable.getSelectionModel().getSelectedItem();
////        if (selected == null) {
////            showAlert("Info", "Please select a guest to edit.");
////            return;
////        }
////
////        Dialog<Guest> dialog = new AddEditGuestDialog(selected);
////        dialog.showAndWait().ifPresent(guest -> {
////            try {
////                guestService.updateGuest(guest);
////                loadGuests();
////            } catch (Exception e) {
////                showAlert("Error", "Failed to update guest: " + e.getMessage());
////            }
////        });
////    }
////
////    @FXML
////    private void handleDeleteGuest() {
////        Guest selected = guestTable.getSelectionModel().getSelectedItem();
////        if (selected == null) {
////            showAlert("Info", "Please select a guest to delete.");
////            return;
////        }
////
////        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
////        alert.setTitle("Delete Guest");
////        alert.setHeaderText("Delete guest ID: " + selected.getGuestID() + "?");
////        alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
////            try {
////                guestService.deleteGuest(selected.getGuestID());
////                loadGuests();
////            } catch (Exception e) {
////                showAlert("Error", "Failed to delete guest: " + e.getMessage());
////            }
////        });
////    }
////
////    private void showAlert(String title, String message) {
////        Alert alert = new Alert(Alert.AlertType.INFORMATION);
////        alert.setTitle(title);
////        alert.setHeaderText(null);
////        alert.setContentText(message);
////        alert.showAndWait();
////    }
////}
//
//package com.college.controller;
//
//import com.college.domain.Guest;
//import com.college.service.GuestService;
//import com.college.service.GuestUIService;
//import javafx.collections.FXCollections;
//import javafx.fxml.FXML;
//import javafx.scene.control.*;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class GuestUIController {
//
//    @FXML
//    private TableView<Guest> guestTable;
//
//    @FXML
//    private TableColumn<Guest, Integer> colGuestID;
//    @FXML
//    private TableColumn<Guest, String> colName;
//    @FXML
//    private TableColumn<Guest, String> colSurname;
//    @FXML
//    private TableColumn<Guest, String> colEmail;
//    @FXML
//    private TableColumn<Guest, String> colContact;
//    @FXML
//    private TableColumn<Guest, String> colPayment;
//
//
//    private final GuestUIService guestService = new GuestUIService();
//
//    @FXML
//    public void initialize() {
//        colGuestID.setCellValueFactory(data ->
//                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getGuestID()).asObject());
//        colName.setCellValueFactory(data ->
//                new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
//        colSurname.setCellValueFactory(data ->
//                new javafx.beans.property.SimpleStringProperty(data.getValue().getSurname()));
//        colEmail.setCellValueFactory(data ->
//                new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
//        colContact.setCellValueFactory(data ->
//                new javafx.beans.property.SimpleStringProperty(data.getValue().getContactNumber()));
//        colPayment.setCellValueFactory(data ->
//                new javafx.beans.property.SimpleStringProperty(data.getValue().getPaymentDetails()));
//
//
//        loadGuests();
//    }
//
//    private void loadGuests() {
//        try {
//            List<Guest> guests = guestService.getAllGuests();
//            guestTable.setItems(FXCollections.observableArrayList(guests));
//        } catch (Exception e) {
//            showAlert("Error", "Failed to load guests: " + e.getMessage());
//        }
//    }
//
//    @FXML
//    private void handleAddGuest() {
//        Dialog<Guest> dialog = new AddEditGuestDialog(null);
//        dialog.showAndWait().ifPresent(guest -> {
//            try {
//                guestService.addGuest(guest);
//                loadGuests();
//            } catch (Exception e) {
//                showAlert("Error", "Failed to add guest: " + e.getMessage());
//            }
//        });
//    }
//
//    @FXML
//    private void handleEditGuest() {
//        Guest selected = guestTable.getSelectionModel().getSelectedItem();
//        if (selected == null) {
//            showAlert("Info", "Please select a guest to edit.");
//            return;
//        }
//
//        Dialog<Guest> dialog = new AddEditGuestDialog(selected);
//        dialog.showAndWait().ifPresent(guest -> {
//            try {
//                guestService.updateGuest(guest);
//                loadGuests();
//            } catch (Exception e) {
//                showAlert("Error", "Failed to update guest: " + e.getMessage());
//            }
//        });
//    }
//
//    @FXML
//    private void handleDeleteGuest() {
//        Guest selected = guestTable.getSelectionModel().getSelectedItem();
//        if (selected == null) {
//            showAlert("Info", "Please select a guest to delete.");
//            return;
//        }
//
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Delete Guest");
//        alert.setHeaderText("Delete guest ID: " + selected.getGuestID() + "?");
//        alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
//            try {
//                guestService.deleteGuest(selected.getGuestID());
//                loadGuests();
//            } catch (Exception e) {
//                showAlert("Error", "Failed to delete guest: " + e.getMessage());
//            }
//        });
//    }
//
//    private void showAlert(String title, String message) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//}


// Newer //
package com.college.controller;

import com.college.domain.Guest;
import com.college.service.GuestService;
import com.college.service.GuestUIService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
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


    private final GuestUIService guestService = new GuestUIService();

    @FXML
    public void initialize() {
        colGuestID.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getGuestID()).asObject());
        colName.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colSurname.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getSurname()));
        colEmail.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        colContact.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getContactNumber()));
        colPayment.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getPaymentDetails()));


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
        Dialog<Guest> dialog = new AddEditGuestDialog(null);
        dialog.showAndWait().ifPresent(guest -> {
            try {
                guestService.addGuest(guest);
                loadGuests();
            } catch (Exception e) {
                showAlert("Error", "Failed to add guest: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleEditGuest() {
        Guest selected = guestTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Info", "Please select a guest to edit.");
            return;
        }

        Dialog<Guest> dialog = new AddEditGuestDialog(selected);
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
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/event-view.fxml"));
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
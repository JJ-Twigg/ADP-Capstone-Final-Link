package com.college.controller;

import com.college.MainFinal;
import com.college.domain.Guest;
import com.college.domain.Reservation;
import com.college.service.ReservationService;
import com.college.utilities.ApplicationContextProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class ReservationUIController implements Initializable {

    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, Integer> reservationIdColumn;
    @FXML private TableColumn<Reservation, String> startTimeColumn;
    @FXML private TableColumn<Reservation, String> endTimeColumn;

    @FXML private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;
    @FXML private Button btnSearch;
    @FXML private Button btnGoBack;
    @FXML private Button btnRefresh;

    @FXML private TextField searchbar;
    @FXML private Label labelFeedback;








    private final ReservationService reservationService;
    private ObservableList<Reservation> reservationList;







    @Autowired
    public ReservationUIController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


    //FK GUEST SET HERE
    private Guest guest; // field to hold the actual Guest object

    public void setGuest(Guest guest) {
        this.guest = guest;
    }










    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reservationIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("reservationDateTimeStart"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("reservationDateTimeEnd"));

        reservationList = FXCollections.observableArrayList();
        reservationTable.setItems(reservationList);

        loadReservationData();
    }

    @FXML
    private void loadReservationData() {
        labelFeedback.setText("");
        List<Reservation> reservations = reservationService.getAll();
        reservationList.clear();
        reservationList.addAll(reservations);
        System.out.println("Data loaded into TableView. Total items: " + reservationList.size());
    }

    @FXML
    private void search(){
        labelFeedback.setText("");
        String searchText = searchbar.getText().trim();

        if (searchText.isEmpty()) {
            labelFeedback.setText("Please enter an ID to search.");
            loadReservationData();
            return;
        }

        try {
            int id = Integer.parseInt(searchText);
            Reservation reservation = reservationService.read(id);

            if (reservation == null){
                System.out.println("No record found for ID: " + id);
                labelFeedback.setText("No record found for ID: " + id);
                reservationList.clear();
            } else {
                reservationList.clear();
                reservationList.add(reservation);
                labelFeedback.setText("Record found for ID: " + id);
            }
        } catch (NumberFormatException e) {
            labelFeedback.setText("Invalid ID. Please enter a number.");
            System.out.println("Invalid search input: " + searchText);
        } finally {
            searchbar.setText("");
        }
    }

    @FXML
    private void getAll() {
        loadReservationData();
    }

    @FXML
    private void add() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dialog_boxes/add-reservation.fxml"));
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean);

            Parent root = loader.load();
            AddReservationController addController = loader.getController();

            addController.setGuest(this.guest);



            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Add New Reservation");
            modalStage.setScene(new Scene(root));
            addController.setStage(modalStage);

            modalStage.showAndWait();
            loadReservationData();

        } catch (IOException e) {
            e.printStackTrace();
            labelFeedback.setText("Error opening Add Reservation form.");
        }
    }

    @FXML
    private void delete() {
        Reservation selectedReservation = reservationTable.getSelectionModel().getSelectedItem();

        if (selectedReservation == null) {
            labelFeedback.setText("Please select a reservation to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Reservation?");
        alert.setContentText("Are you sure you want to delete reservation ID: " + selectedReservation.getReservationId() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean deleted = reservationService.delete(selectedReservation.getReservationId());
                if (deleted) {
                    labelFeedback.setText("Reservation ID: " + selectedReservation.getReservationId() + " deleted successfully.");
                    loadReservationData();
                } else {
                    labelFeedback.setText("Failed to delete reservation ID: " + selectedReservation.getReservationId() + ".");
                }
            } catch (Exception e) {
                e.printStackTrace();
                labelFeedback.setText("Error deleting reservation: " + e.getMessage());
            }
        } else {
            labelFeedback.setText("Deletion cancelled.");
        }
    }

    @FXML
    private void update() {
        Reservation selectedReservation = reservationTable.getSelectionModel().getSelectedItem();

        if (selectedReservation == null) {
            labelFeedback.setText("Please select a reservation to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dialog_boxes/edit-reservation.fxml")); // Use a dedicated FXML for editing
            loader.setControllerFactory(MainFinal.getSpringContext()::getBean);

            Parent root = loader.load();
            EditReservationController editController = loader.getController();

            editController.setReservationToEdit(selectedReservation);

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Edit Reservation (ID: " + selectedReservation.getReservationId() + ")");
            modalStage.setScene(new Scene(root));
            editController.setStage(modalStage);

            modalStage.showAndWait();
            loadReservationData();

        } catch (IOException e) {
            e.printStackTrace();
            labelFeedback.setText("Error opening Edit Reservation form.");
        }
    }
}

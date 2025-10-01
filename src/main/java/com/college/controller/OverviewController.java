package com.college.controller;

import com.college.service.EmployeeService;
import com.college.service.ReservationService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class OverviewController {



    @FXML private CategoryAxis revenueXAxis;
    @FXML private NumberAxis revenueYAxis;

    @FXML
    private PieChart reservationsPieChart;

    @FXML private CategoryAxis totalGuestsChartXAxis;
    @FXML private NumberAxis totalGuestsChartYAxis;

    @FXML
    private Label clockLabel;

    @FXML
    private PieChart totalEmployeesPieChart;

    @FXML
    private Label  userEmailLabel;

    @FXML
    private Label  roleLabel;

    @FXML
    private Label currentGuestsLabel;

    @FXML
    private Label EmployeeLabel;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private EmployeeService employeeService;



    @FXML
    public void initialize() {
//        setupOccupancyChart();

        setupEmployeePieChart();
        setupReservationsPieChart();



        //get live card data via db
        //this live updates when adding new res, therefore it breaks when u call user dash as user dash doesnt use it but ur calling the method

        updateCurrentGuestsLabel();
        
        updateEmployeeCount();
        updateCurrentUserEmail();



        //call clock method
        startClock();
    }

    @FXML
    private Label nameLabel; // link this to FXML

    public void setName(String name) {
        nameLabel.setText(name);
    }

    public void setUserEmail(String email) {
        userEmailLabel.setText(email);
    }

    public void setUserRole(String role) {
        roleLabel.setText(role);
    }

    private void updateCurrentUserEmail() {
        int count = reservationService.getCurrentReservationsCount();
        currentGuestsLabel.setText(String.format("%,d", count));
    }

    private void updateCurrentGuestsLabel() {
        int count = reservationService.getCurrentReservationsCount();
        if (currentGuestsLabel != null) {
            currentGuestsLabel.setText(String.format("%,d", count));
        } else {
            System.out.println("currentGuestsLabel is null cause you called user dashboard, skipping update");
        }

    }

    private void updateEmployeeCount() {
        int totalEmployees = employeeService.getAllEmployees().size();
        if (EmployeeLabel != null) {
            EmployeeLabel.setText(String.valueOf(totalEmployees));
        } else {
            System.out.println("EmployeeLabel is null, cause its for manager and admin dashboard, not user dash, (skipping update)");
        }
    }

    private void setupEmployeePieChart() {
        int totalEmployees = employeeService.getAllEmployees().size();
        int maxCapacity = 50; // optional for visual context

        PieChart.Data employeesSlice = new PieChart.Data("Employees", totalEmployees);
        PieChart.Data remainingSlice = new PieChart.Data("Vacant Slots", maxCapacity - totalEmployees);

        totalEmployeesPieChart.getData().clear();
        totalEmployeesPieChart.getData().addAll(employeesSlice, remainingSlice);

        // Optional: set colors
        Platform.runLater(() -> {
            employeesSlice.getNode().setStyle("-fx-pie-color: #27ae60;"); // green
            remainingSlice.getNode().setStyle("-fx-pie-color: #bdc3c7;"); // gray
        });
    }



    private void setupReservationsPieChart() {
        // Get current reservations from your existing card or service
        int currentReservations = reservationService.getCurrentReservationsCount(); // e.g., 40

        // Fake last month reservations (just for display)
        int lastMonthReservations = (int) (currentReservations * 0.75); // 75% of current, e.g., 30

        PieChart.Data currentSlice = new PieChart.Data("This Month", currentReservations);
        PieChart.Data lastSlice = new PieChart.Data("Last Month", lastMonthReservations);

        reservationsPieChart.getData().clear();
        reservationsPieChart.getData().addAll(currentSlice, lastSlice);

        // Optional: set colors
        Platform.runLater(() -> {
            currentSlice.getNode().setStyle("-fx-pie-color: #27ae60;"); // green
            lastSlice.getNode().setStyle("-fx-pie-color: #3498db;"); // blue
        });
    }


    private void startClock() {
        // Define the format you want
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM dd yyyy  HH:mm:ss");

        // Create a Timeline that updates every second
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime now = LocalDateTime.now();
            clockLabel.setText(now.format(formatter));
        }),
                new KeyFrame(Duration.seconds(1))
        );

        clock.setCycleCount(Timeline.INDEFINITE); // Repeat forever
        clock.play();
    }


    private void setupRevenueChart() {
        XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
        revenueSeries.setName("Revenue");
        revenueSeries.getData().add(new XYChart.Data<>("Jan", 180000));
        revenueSeries.getData().add(new XYChart.Data<>("Feb", 200000));
        revenueSeries.getData().add(new XYChart.Data<>("Mar", 245800));
        revenueSeries.getData().add(new XYChart.Data<>("Apr", 210000));
        revenueSeries.getData().add(new XYChart.Data<>("May", 230000));

    }

    private void setupGuestsChart() {
        int totalEmployees = employeeService.getAllEmployees().size();

        // Set the label
        if (EmployeeLabel != null) {
            EmployeeLabel.setText(String.valueOf(totalEmployees));
        }

        // Optional: show a single bar in chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Employees");
        series.getData().add(new XYChart.Data<>("Total", totalEmployees));


    }



    private void styleBarChartLegend(BarChart<String, Number> chart, String color) {
        chart.getData().forEach(series -> series.getData().forEach(data -> {
            Node node = data.getNode();
            if (node != null) node.setStyle("-fx-bar-fill: " + color + ";");
        }));

        chart.lookupAll(".chart-legend-item-symbol").forEach(node -> node.setStyle("-fx-background-color: " + color + ";"));
    }
}

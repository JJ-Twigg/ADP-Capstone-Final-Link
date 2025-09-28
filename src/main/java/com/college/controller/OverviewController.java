package com.college.controller;

import com.college.service.EmployeeService;
import com.college.service.ReservationService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OverviewController {

    @FXML private PieChart occupancyPieChart;
    @FXML private BarChart<String, Number> revenueBarChart;
    @FXML private CategoryAxis revenueXAxis;
    @FXML private NumberAxis revenueYAxis;
    @FXML private BarChart<String, Number> totalGuestsChart;
    @FXML private CategoryAxis totalGuestsChartXAxis;
    @FXML private NumberAxis totalGuestsChartYAxis;

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
        setupOccupancyChart();
        setupRevenueChart();
        setupGuestsChart();

        // Delay legend styling until after nodes are rendered
        Platform.runLater(() -> {
            stylePieChartLegend();
            styleBarChartLegend(revenueBarChart, "#27ae60"); // dark green
            styleBarChartLegend(totalGuestsChart, "#2ecc71"); // light green
        });

        //get live card data via db
        updateCurrentGuestsLabel();
        updateEmployeeCount();
    }



    private void updateCurrentGuestsLabel() {
        int count = reservationService.getCurrentReservationsCount();
        currentGuestsLabel.setText(String.format("%,d", count)); // formats 1247 -> 1,247
    }

    private void updateEmployeeCount() {
        int totalEmployees = employeeService.getAllEmployees().size();
        EmployeeLabel.setText(String.valueOf(totalEmployees));
    }

    private void setupOccupancyChart() {
        occupancyPieChart.getData().addAll(
                new PieChart.Data("Occupied", 72),
                new PieChart.Data("Available", 28)
        );
    }

    private void setupRevenueChart() {
        XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
        revenueSeries.setName("Revenue");
        revenueSeries.getData().add(new XYChart.Data<>("Jan", 180000));
        revenueSeries.getData().add(new XYChart.Data<>("Feb", 200000));
        revenueSeries.getData().add(new XYChart.Data<>("Mar", 245800));
        revenueSeries.getData().add(new XYChart.Data<>("Apr", 210000));
        revenueSeries.getData().add(new XYChart.Data<>("May", 230000));
        revenueBarChart.getData().add(revenueSeries);
    }

    private void setupGuestsChart() {
        XYChart.Series<String, Number> guestsSeries = new XYChart.Series<>();
        guestsSeries.setName("Guests");
        guestsSeries.getData().add(new XYChart.Data<>("Jan", 120));
        guestsSeries.getData().add(new XYChart.Data<>("Feb", 150));
        guestsSeries.getData().add(new XYChart.Data<>("Mar", 100));
        guestsSeries.getData().add(new XYChart.Data<>("Apr", 180));
        guestsSeries.getData().add(new XYChart.Data<>("May", 200));
        guestsSeries.getData().add(new XYChart.Data<>("Jun", 170));
        totalGuestsChart.getData().add(guestsSeries);
    }

    private void stylePieChartLegend() {
        occupancyPieChart.getData().forEach(data -> {
            data.getNode().setStyle(
                    data.getName().equals("Occupied") ? "-fx-pie-color: #27ae60;" : "-fx-pie-color: #2ecc71;"
            );
        });

        occupancyPieChart.lookupAll(".chart-legend-item-symbol").forEach(node -> {
            node.setStyle(node.toString().contains("Occupied") ? "-fx-background-color: #27ae60;" : "-fx-background-color: #2ecc71;");
        });
    }

    private void styleBarChartLegend(BarChart<String, Number> chart, String color) {
        chart.getData().forEach(series -> series.getData().forEach(data -> {
            Node node = data.getNode();
            if (node != null) node.setStyle("-fx-bar-fill: " + color + ";");
        }));

        chart.lookupAll(".chart-legend-item-symbol").forEach(node -> node.setStyle("-fx-background-color: " + color + ";"));
    }
}

package com.college.controller;

import com.college.domain.User;
import com.college.repository.UserRepository;
import com.college.service.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

@Component
public class OverviewController {



    @FXML private CategoryAxis revenueXAxis;
    @FXML private NumberAxis revenueYAxis;


    //shift chart live
    @FXML
    private BarChart<String, Number> shiftHoursChart;

    //pie chart reservation live
    @FXML
    private PieChart reservationsPieChart;

    //clock live
    @FXML
    private Label clockLabel;


    @FXML private CategoryAxis totalGuestsChartXAxis;
    @FXML private NumberAxis totalGuestsChartYAxis;

    @FXML
    private Label revenueLabel;

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

    @Autowired
    ShiftService shiftService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    private DashboardController dashboardController;

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }


    @FXML
    private BarChart<String, Number> monthlyEarningsBarChart;

    @FXML
    private CategoryAxis monthsAxis;

    @FXML
    private NumberAxis earningsAxis;


    @FXML
    public void initialize() {
//        setupOccupancyChart();

        setupEmployeePieChart();
        setupReservationsPieChart();
        setupShiftHoursChart();


        //get live card data via db
        //this live updates when adding new res, therefore it breaks when u call user dash as user dash doesnt use it but ur calling the method

        updateCurrentGuestsLabel();
        
        updateEmployeeCount();
        updateCurrentUserEmail();
        updateRevenue();

        setupRevenueChart();



        //call clock method
        startClock();
    }

    @FXML
    private Label nameLabel; // link this to FXML

    String userEmail;

    public void setName(String name) {
        nameLabel.setText(name);
    }

    public void setUserEmail(String email) {
        //receives email from dashboard
        userEmailLabel.setText(email);
        this.userEmail = email;

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


    //also a custom method
    private void updateRevenue() {
        Double total = paymentService.getTotalAmount(); // use wrapper Double, since service may return null

        if (revenueLabel!= null) { // <-- this refers to your Label field
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "ZA")); // South Africa Rand
            revenueLabel.setText(currencyFormatter.format(total != null ? total : 0.0));
        } else {
            System.out.println("totalRevenue label is null (not available in this dashboard)");
        }
    }


    // --------------------------------------

    @FXML
    private void updateEmail() {
        System.out.println("\nonMouseClicked");

        Optional<User> userObj = userRepository.findByEmail(userEmailLabel.getText());
        if (userObj.isEmpty()) return;

        TextInputDialog dialog = new TextInputDialog(userEmailLabel.getText());
        dialog.setTitle("Update Email");
        dialog.setHeaderText("Update Email");

        Optional<String> email = dialog.showAndWait();
        if (email.isEmpty()) return;

        String newEmail = email.get().trim();

        // Check if the email already exists
        Optional<User> existingUser = userRepository.findByEmail(newEmail);
        if (existingUser.isPresent() && !existingUser.get().getEmail().equals(userEmailLabel.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Duplicate Email");
            alert.setHeaderText(null);
            alert.setContentText("The email '" + newEmail + "' is already in use. Please choose a different one.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
            return; // stop here
        }

        // Update user in DB
        userObj.get().setEmail(newEmail);
        userRepository.save(userObj.get());

        // Update local label
        setUserEmail(newEmail);

        // Update DashboardController so the new email persists
        if (dashboardController != null) {
            dashboardController.updateEmail(newEmail);
            dashboardController.setUserInfo(newEmail);
        }

        System.out.println("User updated: " + userObj.get());
    }



    @FXML
    private void updateName() {
        System.out.println("\nonMouseClicked");
        String nameString = nameLabel.getText();

        Optional<User> userObj = userRepository.findByEmail(userEmailLabel.getText());
        System.out.println("User: " + userObj);

        TextInputDialog dialog = new TextInputDialog(nameString);
        dialog.setTitle("Update Name");
        dialog.setHeaderText("Update Name");

        Optional<String> name = dialog.showAndWait();
        if (name.isEmpty()) return;

        // Update user object and save to DB
        userObj.get().setName(name.get());
        userRepository.save(userObj.get());

        // Update OverviewController label
        setName(name.get());

        // Update DashboardController so the new name persists
        if (dashboardController != null) {
            dashboardController.updateName(name.get());
        }

        System.out.println("User updated");
        System.out.println(userObj.get());
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

            // Fix legend key colors
            Node[] legendSymbols = totalEmployeesPieChart.lookupAll(".chart-legend-item-symbol").toArray(new Node[0]);
            if (legendSymbols.length >= 2) {
                legendSymbols[0].setStyle("-fx-background-color: #27ae60;"); // "Employees"
                legendSymbols[1].setStyle("-fx-background-color: #bdc3c7;"); // "Vacant Slots"
            }



        });
    }



    private void setupShiftHoursChart() {
        if (shiftHoursChart == null) return;

        // Get total number of shifts from the database
        int totalShifts = shiftService.getAll().size(); // replace with your ShiftService/Repository
        int totalHours = totalShifts * 8; // each shift = 8 hours

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total Hours Worked");

        // Just one bar representing all shifts
        series.getData().add(new XYChart.Data<>("All Shifts", totalHours));

        shiftHoursChart.getData().clear();
        shiftHoursChart.getData().add(series);

        // Optional: style the bar
        Platform.runLater(() -> {
            series.getData().forEach(data -> data.getNode().setStyle("-fx-bar-fill: #808080;"));
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

            // Fix legend key colors
            Node[] legendSymbols = reservationsPieChart.lookupAll(".chart-legend-item-symbol").toArray(new Node[0]);
            if (legendSymbols.length >= 2) {
                legendSymbols[0].setStyle("-fx-background-color: #27ae60;"); // "This Month"
                legendSymbols[1].setStyle("-fx-background-color: #3498db;"); // "Last Month"
            }

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
        Double totalRevenue = paymentService.getTotalAmount();
        if (totalRevenue == null) totalRevenue = 0.0;

        XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
        revenueSeries.setName("Total Revenue");

        // Add a single bar representing total revenue
        revenueSeries.getData().add(new XYChart.Data<>("Total", totalRevenue));

        // Use the correct axes from FXML
        monthsAxis.setLabel("Revenue");
        earningsAxis.setLabel("Amount (R)");

        earningsAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(earningsAxis, "R ", null));

        monthlyEarningsBarChart.getData().clear();
        monthlyEarningsBarChart.getData().add(revenueSeries);

        monthlyEarningsBarChart.setLegendVisible(false);


        Platform.runLater(() -> {
            for (XYChart.Data<String, Number> data : revenueSeries.getData()) {
                if (data.getNode() != null) {
                    data.getNode().setStyle("-fx-bar-fill: #27ae60;"); // green
                }
            }
        });
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



    @FXML
    private void handleUploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                byte[] imageBytes = Files.readAllBytes(selectedFile.toPath());
                // Save to DB
                userService.updateUserPhoto(userEmail, imageBytes);

                // Update ImageView in OverviewController (optional, if you have one)
                // profileImageView.setImage(new Image(new ByteArrayInputStream(imageBytes)));

                // **Update Dashboard ImageView**
                if(dashboardController != null) {
                    dashboardController.setProfileImage(imageBytes);
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Image uploaded successfully!");
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    private ToggleButton themeToggleButton;



    private void styleShiftHoursChartForTheme(boolean darkMode) {
        if (shiftHoursChart == null) return;

        String barColor = darkMode ? "#f1f1f1" : "#808080"; // light gray for dark, gray for light

        shiftHoursChart.getData().forEach(series -> {
            series.getData().forEach(data -> {
                Node node = data.getNode();
                if (node != null) {
                    node.setStyle("-fx-bar-fill: " + barColor + ";");
                }
            });
        });
    }




    @FXML
    private void toggleTheme(ActionEvent event) {
        boolean dark = themeToggleButton.isSelected();

        // change card + other UI themes (you already have this)
        Scene scene = themeToggleButton.getScene();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource(dark ? "/css/darkTheme.css" : "/css/lightTheme.css").toExternalForm());

        // update chart colors to match cards
        updateShiftChartColor(dark);

        themeToggleButton.setText(dark ? "Light Mode" : "Dark Mode");
    }

    private void updateShiftChartColor(boolean darkMode) {
        Platform.runLater(() -> {
            String color = darkMode ? "#333333" : "#808080"; // dark card vs light card solid color

            for (XYChart.Series<String, Number> series : shiftHoursChart.getData()) {
                for (XYChart.Data<String, Number> data : series.getData()) {
                    Node bar = data.getNode();
                    if (bar != null) {
                        bar.setStyle("-fx-bar-fill: " + color + ";");
                    }
                }
            }
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

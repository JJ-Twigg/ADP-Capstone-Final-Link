package com.college.controller;

import com.college.domain.Room;
import com.college.factory.RoomFactory;
import com.college.service.RoomService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class RoomController {

    private Integer reservationId; // FK for Event

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    @Autowired
    private RoomService roomService;

    private Room room;

    public RoomController() {
        // No-arg constructor (can be empty)
    }

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
        room = RoomFactory.createRoom(1, "Single", 100.0f, true, "WiFi, TV");
    }

    private final Set<Integer> addedRooms = new HashSet<>();

    // Buttons
    @FXML private Button buttonA;
    @FXML private Button buttonB;
    @FXML private Button buttonC;
    @FXML private Button buttonD;

    // Editable Labels and TextFields for Price
    @FXML private Label priceLabel;
    @FXML private TextField priceTextField;

    // Editable Labels and TextFields for Room Type
    @FXML private Label typeLabel;
    @FXML private TextField typeTextField;

    // Editable Labels and TextFields for Availability
    @FXML private Label availabilityLabel;
    @FXML private TextField availabilityTextField;

    // Editable Labels and TextFields for Features
    @FXML private Label featuresLabel;
    @FXML private TextField featuresTextField;

    // --- Card A Icons ---
    @FXML private ImageView priceIconA, typeIconA, availabilityIconA, featuresIconA;

    // --- Card B Icons ---
    @FXML private ImageView priceIconB, typeIconB, availabilityIconB, featuresIconB;

    // --- Card C Icons ---
    @FXML private ImageView priceIconC, typeIconC, availabilityIconC, featuresIconC;

    // --- Card D Icons ---
    @FXML private ImageView priceIconD, typeIconD, availabilityIconD, featuresIconD;

    // --- Hover Handlers for all green icons ---
    @FXML
    private void onIconMouseEntered(MouseEvent event) {
        ImageView icon = (ImageView) event.getSource();
        icon.setOpacity(0.7); // example hover effect
    }

    @FXML
    private void onIconMouseExited(MouseEvent event) {
        ImageView icon = (ImageView) event.getSource();
        icon.setOpacity(1.0); // reset when mouse exits
    }

    //one handler for all booking buttons
    @FXML
    private void handleBookClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource(); // the button that fired the event
        String id = clickedButton.getId(); // fx:id from FXML
//        clickedButton.setDisable(true);

        //traverse JavaFx dom, event Source to find the button that was clicked
        VBox cardVBox = (VBox) clickedButton.getParent();

        HBox priceBox = (HBox) cardVBox.getChildren().get(2);      // index 2
        HBox typeBox = (HBox) cardVBox.getChildren().get(3);       // index 3 etc
        HBox availabilityBox = (HBox) cardVBox.getChildren().get(4);
        HBox featuresBox = (HBox) cardVBox.getChildren().get(5);

        String priceText = ((Label) priceBox.getChildren().get(0)).getText();
        String typeText = ((Label) typeBox.getChildren().get(0)).getText();
        String availabilityText = ((Label) availabilityBox.getChildren().get(0)).getText();
        String featuresText = ((Label) featuresBox.getChildren().get(0)).getText();

        // clean text values
        String roomType = typeText.replace("Room Type:", "").trim();
        float price = Float.parseFloat(priceText.replaceAll("[^\\d.]", ""));
        boolean available = availabilityText.toLowerCase().contains("yes");
        String features = featuresText.replace("Features:", "").trim();

        System.out.println("Room Type: " + roomType);
        System.out.println("Price: " + price);
        System.out.println("Available: " + available);
        System.out.println("Features: " + features);


        int roomID = 0;

        // Determine roomID based on button
        switch (id) {
            // Page 1
            case "buttonA": roomID = 51; break;
            case "buttonB": roomID = 52; break;
            case "buttonC": roomID = 53; break;
            case "buttonD": roomID = 54; break;

            //  2
            case "buttonE": roomID = 55; break;
            case "buttonF": roomID = 56; break;
            case "buttonG": roomID = 57; break;
            case "buttonH": roomID = 58; break;

            // 3
            case "buttonI": roomID = 59; break;
            case "buttonJ": roomID = 60; break;
            case "buttonK": roomID = 61; break;
            case "buttonL": roomID = 62; break;
        }

        // Create the Room object
        Room room = RoomFactory.createRoom(roomID, roomType, price, available, features);

        //call update regardless of button text
        if (clickedButton.getText().equals(" Update ")) {
            System.out.println("Updating room: " + roomType);
            roomService.update(room);
        } else {
            System.out.println(roomType + " update ");
            roomService.update(room);
        }

        // Consume RESTful service
        String jsonPayload = String.format(
                "{\"roomType\":\"%s\", \"price\":%.2f, \"available\":%b, \"features\":\"%s\"}",
                roomType, price, available, features
        );

//        endPoint(jsonPayload);

        System.out.println(room.toString());

    }

//    private void endPoint(String jsonPayload) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);
//
//        try {
//
//            //call my url
//            String responseSpring = restTemplate.postForObject(
//                    "http://localhost:8080/api/book-room",
//                    request,
//                    String.class
//            );
//
//            System.out.println("Response: " + responseSpring);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @RestController
//    @RequestMapping("/api")
//    public class MockController {
//
//        @PostMapping("/book-room")
//        public ResponseEntity<String> bookRoom(@RequestBody String payload) {
//            System.out.println("Received payload: " + payload);
//            return ResponseEntity.ok("{\"status\":\"success\"}");
//        }
//    }

    // --- Edit Price ---
    @FXML
    private void editPrice(MouseEvent event) {
        ImageView clickedIcon = (ImageView) event.getSource();
        HBox hbox = (HBox) clickedIcon.getParent();

        Label priceLabel = null;
        TextField priceTextField = null;
        for (Node node : hbox.getChildren()) {
            if (node instanceof Label) {
                priceLabel = (Label) node;
            } else if (node instanceof TextField) {
                priceTextField = (TextField) node;
            }
        }

        if (priceLabel != null && priceTextField != null) {
            // Extract current price (numbers only) from label text
            String currentPrice = priceLabel.getText().replaceAll("[^0-9]", "").trim();
            priceTextField.setText(currentPrice);

            // Only allow integer input
            priceTextField.setTextFormatter(new javafx.scene.control.TextFormatter<>(change -> {
                if (change.getText().matches("[0-9]*")) {
                    return change;
                }
                return null;
            }));

            // Toggle visibility: hide label, show textfield for editing
            priceLabel.setVisible(false);
            priceLabel.setManaged(false);
            priceTextField.setVisible(true);
            priceTextField.setManaged(true);

            priceTextField.requestFocus();
        }
    }


    @FXML
    private void priceTextFieldOnEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            TextField textField = (TextField) event.getSource();
            HBox hbox = (HBox) textField.getParent();

            Label priceLabel = null;
            for (Node node : hbox.getChildren()) {
                if (node instanceof Label) {
                    priceLabel = (Label) node;
                    break;
                }
            }

            if (priceLabel != null) {
                String newPrice = textField.getText().trim();
                priceLabel.setText("Price Per Night: R" + newPrice);

                // Toggle visibility back
                priceLabel.setVisible(true);
                priceLabel.setManaged(true);
                textField.setVisible(false);
                textField.setManaged(false);
            }
        }
    }

    @FXML
    private void editType(MouseEvent event) {
        ImageView clickedIcon = (ImageView) event.getSource();
        HBox hbox = (HBox) clickedIcon.getParent();

        Label typeLabel = null;
        TextField typeTextField = null;
        for (Node node : hbox.getChildren()) {
            if (node instanceof Label) {
                typeLabel = (Label) node;
            } else if (node instanceof TextField) {
                typeTextField = (TextField) node;
            }
        }

        if (typeLabel != null && typeTextField != null) {
            // Extract current type text (after colon)
            String currentType = typeLabel.getText().split(":")[1].trim();
            typeTextField.setText(currentType);

            typeLabel.setVisible(false);
            typeLabel.setManaged(false);
            typeTextField.setVisible(true);
            typeTextField.setManaged(true);

            typeTextField.requestFocus();
        }
    }

    @FXML
    private void editAvailability(MouseEvent event) {
        ImageView clickedIcon = (ImageView) event.getSource();
        HBox hbox = (HBox) clickedIcon.getParent();

        Label availabilityLabel = null;
        TextField availabilityTextField = null;
        for (Node node : hbox.getChildren()) {
            if (node instanceof Label) {
                availabilityLabel = (Label) node;
            } else if (node instanceof TextField) {
                availabilityTextField = (TextField) node;
            }
        }

        if (availabilityLabel != null && availabilityTextField != null) {
            // Extract current availability (after colon)
            String currentAvailability = availabilityLabel.getText().split(":")[1].trim();
            availabilityTextField.setText(currentAvailability);

            availabilityLabel.setVisible(false);
            availabilityLabel.setManaged(false);
            availabilityTextField.setVisible(true);
            availabilityTextField.setManaged(true);

            availabilityTextField.requestFocus();
        }
    }

    @FXML
    private void editFeatures(MouseEvent event) {
        ImageView clickedIcon = (ImageView) event.getSource();
        HBox hbox = (HBox) clickedIcon.getParent();

        Label featuresLabel = null;
        TextField featuresTextField = null;
        for (Node node : hbox.getChildren()) {
            if (node instanceof Label) {
                featuresLabel = (Label) node;
            } else if (node instanceof TextField) {
                featuresTextField = (TextField) node;
            }
        }

        if (featuresLabel != null && featuresTextField != null) {
            // Extract current features (after colon)
            String currentFeatures = featuresLabel.getText().split(":")[1].trim();
            featuresTextField.setText(currentFeatures);

            featuresLabel.setVisible(false);
            featuresLabel.setManaged(false);
            featuresTextField.setVisible(true);
            featuresTextField.setManaged(true);

            featuresTextField.requestFocus();
        }
    }

    @FXML
    private void typeTextFieldOnEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            TextField textField = (TextField) event.getSource();
            HBox hbox = (HBox) textField.getParent();

            Label typeLabel = null;
            for (Node node : hbox.getChildren()) {
                if (node instanceof Label) {
                    typeLabel = (Label) node;
                    break;
                }
            }

            if (typeLabel != null) {
                String newType = textField.getText().trim();
                if (!newType.isEmpty()) {
                    typeLabel.setText("Room Type: " + newType + " ");
                }

                // Toggle visibility
                typeLabel.setVisible(true);
                typeLabel.setManaged(true);
                textField.setVisible(false);
                textField.setManaged(false);
            }
        }
    }


    @FXML
    private void availabilityTextFieldOnEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            TextField textField = (TextField) event.getSource();
            HBox hbox = (HBox) textField.getParent();

            Label availabilityLabel = null;
            for (Node node : hbox.getChildren()) {
                if (node instanceof Label) {
                    availabilityLabel = (Label) node;
                    break;
                }
            }

            if (availabilityLabel != null) {
                String newAvailability = textField.getText().trim();
                if (!newAvailability.isEmpty()) {
                    availabilityLabel.setText("Availability: " + newAvailability);
                }

                availabilityLabel.setVisible(true);
                availabilityLabel.setManaged(true);
                textField.setVisible(false);
                textField.setManaged(false);
            }
        }
    }

    @FXML
    private void featuresTextFieldOnEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            TextField textField = (TextField) event.getSource();
            HBox hbox = (HBox) textField.getParent();

            Label featuresLabel = null;
            for (Node node : hbox.getChildren()) {
                if (node instanceof Label) {
                    featuresLabel = (Label) node;
                    break;
                }
            }

            if (featuresLabel != null) {
                String newFeatures = textField.getText().trim();
                if (!newFeatures.isEmpty()) {
                    featuresLabel.setText("Features: " + newFeatures + " ");
                }

                featuresLabel.setVisible(true);
                featuresLabel.setManaged(true);
                textField.setVisible(false);
                textField.setManaged(false);
            }
        }
    }




    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }


    @FXML
    private void nextPage(ActionEvent event) {
        try {
            System.out.println("\nPage2, loading...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-room-page2.fxml"));
            Parent page2 = loader.load();

            // Get the controller to manually inject the service
            RoomController page2Controller = loader.getController();
            page2Controller.setRoomService(this.roomService); // pass your existing service

            // Replace the current root
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(page2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-room-page1.fxml"));
            Parent mainViewRoot = loader.load();

            // Inject the same service back into the controller
            RoomController mainController = loader.getController();
            mainController.setRoomService(this.roomService);

            // Replace the scene root
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(mainViewRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- REST API Endpoints (Spring Boot) ---
    @RequestMapping("/test")
    Room save(Room room) {
        return roomService.create(room);
    }

    @RequestMapping("/read")
    String read(int id) {
        roomService.read(id);
        return "welcome";
    }

    @RequestMapping("/update")
    String update(Room room) {
        roomService.update(room);
        return "welcome";
    }

    @RequestMapping("/delete")
    String delete(int id) {
        roomService.delete(id);
        return "welcome";
    }
}

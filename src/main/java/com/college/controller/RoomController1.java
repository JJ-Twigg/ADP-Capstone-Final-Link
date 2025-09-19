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

import java.io.IOException;



@Component
@Deprecated
public class RoomController1 {

    @Autowired
    private RoomService roomService;

    private Room room;

    public RoomController1() {}
    public RoomController1(RoomService roomService) {
        this.roomService = roomService;
        room = RoomFactory.createRoom(1, "Single", 100.0f, true, "WiFi, TV");
    }

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


    //page 2. No dice

//    // --- Card E Icons ---
//    @FXML private ImageView priceIconE, typeIconE, availabilityIconE, featuresIconE;
//
//    // --- Card F Icons ---
//    @FXML private ImageView priceIconF, typeIconF, availabilityIconF, featuresIconF;
//
//    // --- Card G Icons ---
//    @FXML private ImageView priceIconG, typeIconG, availabilityIconG, featuresIconG;
//
//    // --- Card H Icons ---
//    @FXML private ImageView priceIconH, typeIconH, availabilityIconH, featuresIconH;



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

        // Decide action based on button text
        if (clickedButton.getText().equals("Update")) {
            System.out.println("Updating room: " + roomType);
            roomService.update(room); // call update
        } else {
            System.out.println(roomType + " Add");
            clickedButton.setText("Update");
            roomService.create(room); // first-time booking
        }

        // Consume RESTful service
        String jsonPayload = String.format(
                "{\"roomType\":\"%s\", \"price\":%.2f, \"available\":%b, \"features\":\"%s\"}",
                roomType, price, available, features
        );

//        endPoint(jsonPayload);

        System.out.println(room.toString());

    }

    //have to re make all icons visible = false if i want hover again.
    //

//    @FXML
//    public void initialize() {
//        // --- Page 1 ---
//        safeSetupHover(priceIconA); safeSetupHover(typeIconA); safeSetupHover(availabilityIconA); safeSetupHover(featuresIconA);
//        safeSetupHover(priceIconB); safeSetupHover(typeIconB); safeSetupHover(availabilityIconB); safeSetupHover(featuresIconB);
//        safeSetupHover(priceIconC); safeSetupHover(typeIconC); safeSetupHover(availabilityIconC); safeSetupHover(featuresIconC);
//        safeSetupHover(priceIconD); safeSetupHover(typeIconD); safeSetupHover(availabilityIconD); safeSetupHover(featuresIconD);
//
//        // --- Page 2 Icons ---
////        safeSetupHover(priceIconE); safeSetupHover(typeIconE); safeSetupHover(availabilityIconE); safeSetupHover(featuresIconE);
////        safeSetupHover(priceIconF); safeSetupHover(typeIconF); safeSetupHover(availabilityIconF); safeSetupHover(featuresIconF);
////        safeSetupHover(priceIconG); safeSetupHover(typeIconG); safeSetupHover(availabilityIconG); safeSetupHover(featuresIconG);
////        safeSetupHover(priceIconH); safeSetupHover(typeIconH); safeSetupHover(availabilityIconH); safeSetupHover(featuresIconH);
//
//
//
//    }


//    private void safeSetupHover(ImageView icon) {
//        if (icon == null) {
//            System.err.println("Warning: ImageView is null in setupHover");
//            return;
//        }
//
//        icon.setVisible(false); // hide icon by default
//
//        if (icon.getParent() != null) {
//            icon.getParent().setOnMouseEntered(e -> icon.setVisible(true));
//            icon.getParent().setOnMouseExited(e -> icon.setVisible(false));
//        }
//    }

    //permanent button name if a room is already on database
//    @FXML
//    public void initialize() {
//        Map<Integer, Button> roomButtonMap = new HashMap<>();
//        roomButtonMap.put(51, buttonA);
//        roomButtonMap.put(52, buttonB);
//        roomButtonMap.put(53, buttonC);
//        roomButtonMap.put(54, buttonD);
//
//        for (Map.Entry<Integer, Button> entry : roomButtonMap.entrySet()) {
//            int roomID = entry.getKey();
//            Button button = entry.getValue();
//
//            boolean roomExists = roomService.read(roomID) != null;
//            button.setText(roomExists ? "Update" : "Add");
//        }
//    }


    //2 ways of calling an endpoint url. Make one, or use public one

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

    // --- Toggle helper for editing Labels/TextFields ---
    private void toggleEdit(Label label, TextField textField) {
        boolean editing = textField.isVisible();
        label.setVisible(editing);
        label.setManaged(editing);
        textField.setVisible(!editing);
        textField.setManaged(!editing);
        if (!editing) {
            textField.requestFocus();
            textField.selectAll();
        }
    }

    // --- Edit Price ---
    @FXML
    private void editPrice(MouseEvent event) {
        ImageView clickedIcon = (ImageView) event.getSource();
        // The parent HBox of clicked icon
        HBox hbox = (HBox) clickedIcon.getParent();

        // Find the Label and TextField inside this HBox
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


    // --- Navigation ---

    //old doesnt work with buttons or hover on 2nd page
//    @FXML
//    private void nextPage(ActionEvent event) {
//        System.out.println("Button clicked!");
//        try {
//            Parent page2Root = FXMLLoader.load(getClass().getResource("/pageTwo.fxml"));
//            Scene currentScene = ((Node) event.getSource()).getScene();
//            currentScene.setRoot(page2Root);
//            System.out.println("Moving to new page");
//        } catch (IOException e) {
//            e.printStackTrace();
//
//        }
//    }


    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }


    @FXML
    private void nextPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pageTwo.fxml"));
            Parent page2 = loader.load();

            // Get the controller to manually inject the service
            RoomController1 page2Controller = loader.getController();
            page2Controller.setRoomService(this.roomService); // pass your existing service

            // Replace the current root
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(page2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Page3(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pageThree.fxml")); // adjust path
            Parent page3 = loader.load();

            // Get controller to inject services if needed
            RoomController1 page3Controller = loader.getController();
            page3Controller.setRoomService(this.roomService); // reuse same service

            // Replace current scene root
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(page3);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent mainViewRoot = FXMLLoader.load(getClass().getResource("/scenes/window-room-page1.fxml"));
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(mainViewRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // --- REST API Endpoints (Spring Boot) ---
//    @RequestMapping("/test")
//    Room save(Room room) {
//        return roomService.create(room);
//    }
//
//    @RequestMapping("/read")
//    String read(int id) {
//        roomService.read(id);
//        return "welcome";
//    }
//
//    @RequestMapping("/update")
//    String update(Room room) {
//        roomService.update(room);
//        return "welcome";
//    }
//
//    @RequestMapping("/delete")
//    String delete(int id) {
//        roomService.delete(id);
//        return "welcome";
//    }
}

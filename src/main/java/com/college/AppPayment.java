/*
Project:    Hotel-management system
Started:    11.03.25
Updated:    25.08.25
---

Members
-------
Name and surname    | Student id    | GitHub repo                                                       |
---------------------------------------------------------------------------------------------------------
Ammaar Swartland    | 230159478     | (original) https://github.com/ammaar0x01/ADP3_capstone_project    |
Zaid Theunissen     | 221084142     | https://github.com/zaid-xt/ADP3_capstone_project                  |
Joshua Twigg        | 222153881     | https://github.com/JoshuaTwigg/ADP3_capstone_project              |
Talia Theresa Smuts | 221126082     | https://github.com/Taliasmuts28/ADP3_capstone_project             |
Muaath Slamong      | 230074138     | https://github.com/MuaathSlamong-alt/ADP3_capstone_project        |
---------------------------------------------------------------------------------------------------------

Responsibilities
-----------------------------------------------------
Ammaar Swartland    | Reservation classes           |
Zaid Theunissen     | Guest classes                 |
Joshua Twigg        | Room classes                  |
Talia Theresa Smuts | Payment classes               |
Muaath Slamong      | Housekeeper classes           |
-----------------------------------------------------
*/

//package com.college;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class Main {
//    public static void main(String[] args) {
//        SpringApplication.run(Main.class, args);
//    }
//}


package com.college;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AppPayment extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void init() throws Exception {
        // Start Spring Boot context
        springContext = SpringApplication.run(AppPayment.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load FXML with Spring controller factory
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-payment.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-payment-d1.fxml"));
        loader.setControllerFactory(springContext::getBean);

        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);

        primaryStage.setTitle("HMS - Payment Management");

        scene.getStylesheets().add(getClass().getResource("/css/buttonStyle.css").toExternalForm());

        Image icon = new Image(getClass().getResourceAsStream("/images/icons/bed.png"));
        primaryStage.getIcons().add(icon);

        primaryStage.setWidth(1000);
        primaryStage.setHeight(600);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        // Close Spring context when JavaFX closes
        springContext.close();
        Platform.exit();
    }

    public static void main(String[] args) {
        // Disable Spring Boot's web server since we're using JavaFX
//        System.setProperty("java.awt.headless", "false");
//        System.setProperty("spring.main.web-application-type", "none");

        // Launch JavaFX application
        Application.launch(AppPayment.class, args);
    }
}

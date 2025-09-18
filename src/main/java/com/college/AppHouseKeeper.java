package com.college;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AppHouseKeeper extends Application {
    private ConfigurableApplicationContext context;

    @Override
//    public void init() {
    public void init() throws Exception{
//        context = new SpringApplicationBuilder(BootApp.class).run();
//        context = new SpringApplicationBuilder(Main.class).run();
        context = SpringApplication.run(AppHouseKeeper.class);
    }


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-housekeeper-d1.fxml"));
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-housekeeper.fxml"));
        loader.setControllerFactory(context::getBean);

        // ------------------
        // newer
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/css/buttonStyle.css").toExternalForm());

//        Image icon = new Image(getClass().getResourceAsStream("/images/icons/bed.png"));
//        stage.getIcons().add(icon);
        // ------------------

        stage.setTitle("HMS - House-keeper Management");
        Image icon = new Image(getClass().getResourceAsStream("/images/icons/bed.png"));
        stage.getIcons().add(icon);

//        stage.setScene(new Scene(loader.load(), 1000, 600));
        stage.setResizable(false);
        stage.setWidth(1000);
        stage.setHeight(600);
        stage.show();
    }

    @Override
    public void stop() {
        if (context != null) {
            context.close();
        }
    }

    public static void main(String[] args) {
        // Disable Spring Boot's web server since we're using JavaFX
//        System.setProperty("java.awt.headless", "false");
//        System.setProperty("spring.main.web-application-type", "none");

        // Launch JavaFX application
        Application.launch(AppHouseKeeper.class, args);
    }
}

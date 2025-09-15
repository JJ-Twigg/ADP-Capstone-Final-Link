package com.college;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class AppJFoodWorker extends Application {

    private ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        // Start Spring before JavaFX
        springContext = new SpringApplicationBuilder(Main.class)
                .run(); // your SpringBootApplication class
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("\nLoading FoodWorker scene...");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-foodWorker.fxml"));

        // Give FXMLLoader access to Spring beans
        loader.setControllerFactory(springContext::getBean);

        Parent root = loader.load();
        Scene scene = new Scene(root);

        // Apply CSS for FoodWorker scene
        scene.getStylesheets().add(getClass().getResource("/css/buttonStyle.css").toExternalForm());

        // FoodWorker icon
        Image icon = new Image(getClass().getResourceAsStream("/images/icons/bed.png"));
        stage.getIcons().add(icon);

        stage.setScene(scene);
        stage.setTitle("HMS - FoodWorker Management");
        stage.setWidth(1000);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() {
        // Shutdown Spring when JavaFX exits
        springContext.close();
    }
}
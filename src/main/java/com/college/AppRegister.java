package com.college;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;


public class AppRegister extends Application {

    private static ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        springContext = new SpringApplicationBuilder(Main.class).run();
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("\nLoading Register scene...");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-sign-up.fxml"));

        // Give FXMLLoader access to Spring beans
        loader.setControllerFactory(springContext::getBean);

        Parent root = loader.load();
        Scene scene = new Scene(root);

        // Apply CSS if needed (optional)
        scene.getStylesheets().add(getClass().getResource("/css/buttonStyle.css").toExternalForm());

        // Register icon
        Image icon = new Image(getClass().getResourceAsStream("/images/icons/bed.png"));
        stage.getIcons().add(icon);

        stage.setScene(scene);
        stage.setTitle("Hotel Management System");
//        stage.setWidth(1000);
//        stage.setHeight(600);
        stage.setResizable(false);
        stage.show();


    }

    @Override
    public void stop() {
        // Shutdown Spring when exit clicked
        springContext.close();
    }

    //get context of loader.setControllerFactory(AppRegister.getSpringContext()::getBean);
    //this is needed for login page to work.
    public static ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }
}

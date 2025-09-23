package com.college;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URL;

public class AppGuest extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        springContext = MainApp.getSpringContext();
    }

    @Override
    public void start(Stage stage) throws Exception {



        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/scenes/guest-view-updated.fxml"));


        //causing null error
//        fxmlLoader.setControllerFactory(springContext::getBean);

        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        stage.setTitle("HMS - Guest Management");



//        Image icon = new Image(getClass().getResourceAsStream("/images/icons/bed.png"));
//        stage.getIcons().add(icon);

        stage.setScene(scene);
        stage.setWidth(1000);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() {
        springContext.close();
    }
}

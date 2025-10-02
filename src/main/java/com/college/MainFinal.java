package com.college;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MainFinal extends Application {

    private static ConfigurableApplicationContext springContext;

    public static ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }

    @Override
    public void init() {
        springContext = new SpringApplicationBuilder(MainFinal.class).run();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-sign-upFinal.fxml"));
        loader.setControllerFactory(springContext::getBean);
        Parent root = loader.load();


        Image icon = new Image(getClass().getResourceAsStream("/images/icons/bed.png"));
        stage.getIcons().add(icon);

        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("/css/buttonStyle.css").toExternalForm());

    // remove this and it will deafult back to default
        stage.setWidth(616);
        stage.setHeight(440);


        stage.setScene(scene);
        stage.setTitle("HMS - Room Management");
        stage.show();

        System.out.println("Stage width: " + stage.getWidth());
        System.out.println("Stage height: " + stage.getHeight());
    }

    @Override
    public void stop() {
        springContext.close();
    }
}

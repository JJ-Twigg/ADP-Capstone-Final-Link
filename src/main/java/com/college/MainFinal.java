package com.college;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-sign-up.fxml"));
        loader.setControllerFactory(springContext::getBean);
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void stop() {
        springContext.close();
    }
}

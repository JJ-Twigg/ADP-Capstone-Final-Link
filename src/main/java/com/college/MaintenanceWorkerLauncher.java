package com.college;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class MaintenanceWorkerLauncher extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        context = new SpringApplicationBuilder(Main.class).run();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MaintenanceWorker-View.fxml"));
        loader.setControllerFactory(context::getBean);
        stage.setTitle("Maintenance Worker CRUD");
//        stage.setScene(new Scene(loader.load(), 800, 600));
        stage.setScene(new Scene(loader.load(), 1000, 600));
        stage.show();
    }

    @Override
    public void stop() {
        if (context != null) context.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
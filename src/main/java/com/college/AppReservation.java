package com.college;

import com.college.utilities.ApplicationContextProvider;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class AppReservation extends Application {
    private ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        springContext = new SpringApplicationBuilder(com.college.MainApp.class).run();
        ApplicationContextProvider.setApplicationContext(springContext);
    }

    @Override
    public void start(Stage stage) throws Exception {
        String sceneName = "/scenes/window-reservation.fxml";
        System.out.println("\n>>> Loading scene from '" + sceneName + "'...");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName));

        // Give FXMLLoader access to Spring beans
        loader.setControllerFactory(springContext::getBean);

        Parent root = loader.load();
        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("/css/buttonStyle.css").toExternalForm());
//        scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());

        Image icon = new Image(getClass().getResourceAsStream("/images/icons/bed.png"));
        stage.getIcons().add(icon);

        stage.setTitle("HMS - Reservations Management");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setWidth(1000);
        stage.setHeight(600);
        stage.show();
    }

    @Override
    public void stop() {
        springContext.close();
    }

    // =============================================
    // MAIN //
    public static void main(String[] args) {
        launch(args);
    }
    // =============================================
}

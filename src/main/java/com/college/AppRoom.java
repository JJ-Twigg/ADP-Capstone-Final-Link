//package com.college;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.image.Image;
//import javafx.stage.Stage;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.context.ConfigurableApplicationContext;
//
//public class AppJ extends Application {
//    private ConfigurableApplicationContext springContext;
//    // =============================================
//
//    @Override
//    public void init() {
//        springContext = new SpringApplicationBuilder(Main.class).run();
//    }
//
//    @Override
//    public void start(Stage stage) throws Exception {
////        String sceneName = "/scenes/window-room-page1.fxml";
//        String sceneName = "window-room-page1.fxml";
//
//        System.out.println("\n>>> Loading scene from '" + sceneName + "'...");
//        FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName));
//        loader.setControllerFactory(springContext::getBean);
//
//        Parent root = loader.load();
//        Scene scene = new Scene(root);
//
////        scene.getStylesheets().add(getClass().getResource("/css/buttonStyle.css").toExternalForm());
////        scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
//
//        Image icon = new Image(getClass().getResourceAsStream("/images/icons/app-icon.png"));
//        stage.getIcons().add(icon);
//
//        stage.setScene(scene);
//        stage.setTitle("HMS - Room Management");
//        stage.setWidth(1000);
//        stage.setHeight(600);
//        stage.setResizable(false);
//
//        System.out.println("\nRendering UI...");
//        stage.show();
//    }
//
//    @Override
//    public void stop() {
//        springContext.close();
//    }
//
//    // =============================================
//    // MAIN //
//    public static void main(String[] args) {
//        launch(args);
//    }
//    // =============================================
//}


package com.college;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class AppRoom extends Application {

    private ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        // Start Spring before JavaFX
        springContext = new SpringApplicationBuilder(com.college.MainApp.class).run();
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("\nloading...");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/window-room-page1.fxml"));

        // Give FXMLLoader access to Spring beans
        loader.setControllerFactory(springContext::getBean);

        Parent root = loader.load();
        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("/css/buttonStyle.css").toExternalForm());

        Image icon = new Image(getClass().getResourceAsStream("/images/icons/bed.png"));
        stage.getIcons().add(icon);

        stage.setScene(scene);
        stage.setTitle("HMS - Room Management");
        stage.setWidth(1000);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() {
        //shutdown Spring when JavaFX exits
        springContext.close();
    }
}



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

import com.college.domain.Room;
import com.college.factory.RoomFactory;
import com.college.repository.RoomRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class AppRoom extends Application {

    private ConfigurableApplicationContext springContext;

    @Autowired
    RoomRepository roomRepository;

    public static void main(String[] args) {
        launch(args);
    }




    @Override
    public void init() {
        // Start Spring before JavaFX
        springContext = new SpringApplicationBuilder(com.college.MainApp.class).run();

        seedRooms();


    }

    @Transactional
    private void seedRooms() {
        RoomRepository roomRepository = springContext.getBean(RoomRepository.class);

        // Optional: clear table if you want a fresh start every launch
        roomRepository.deleteAll();

        // Create and save rooms
        List<Room> rooms = List.of(
                RoomFactory.createRoom(51, "VIP", 2000f, true, "WiFi, TV, Room service"),
                RoomFactory.createRoom(52, "VIP", 2000f, true, "WiFi, TV, Balcony"),
                RoomFactory.createRoom(53, "Standard", 1500f, false, "WiFi, TV"),
                RoomFactory.createRoom(54, "Economy", 1200f, true, "Basic Amenities"),
                RoomFactory.createRoom(55, "VIP", 1800f, true, "WiFi, TV, Balcony"),
                RoomFactory.createRoom(56, "Standard", 1600f, false, "WiFi, TV"),
                RoomFactory.createRoom(57, "VIP", 2000f, true, "WiFi, TV"),
                RoomFactory.createRoom(58, "Economy", 1400f, true, "WiFi, TV, Balcony"),
                RoomFactory.createRoom(59, "Standard", 1700f, false, "WiFi, TV")
        );

        roomRepository.saveAll(rooms); // batch save is safer than multiple save()
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



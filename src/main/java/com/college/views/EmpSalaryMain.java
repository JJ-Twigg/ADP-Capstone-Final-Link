
       package com.college.views;

       import javafx.application.Application;
       import javafx.application.Platform;
       import javafx.fxml.FXMLLoader;
       import javafx.scene.Scene;
       import javafx.stage.Stage;
       import org.springframework.boot.SpringApplication;
       import org.springframework.boot.autoconfigure.SpringBootApplication;
       import org.springframework.context.ConfigurableApplicationContext;

       @SpringBootApplication
       public class EmpSalaryMain extends Application {

           private ConfigurableApplicationContext springContext;

           @Override
           public void init() throws Exception {
               // Start Spring Boot context
               springContext = SpringApplication.run(EmpSalaryMain.class);
           }

           @Override
           public void start(Stage primaryStage) throws Exception {
               // Load FXML with Spring controller factory
               FXMLLoader loader = new FXMLLoader(getClass().getResource("/EmployeeSalary.fxml"));
               loader.setControllerFactory(springContext::getBean);

               Scene scene = new Scene(loader.load());
               primaryStage.setTitle("Employee Salary Management");
               primaryStage.setScene(scene);
               primaryStage.show();
           }

           @Override
           public void stop() throws Exception {
               // Close Spring context when JavaFX closes
               springContext.close();
               Platform.exit();
           }

           public static void main(String[] args) {
               // Disable Spring Boot's web server since we're using JavaFX
               System.setProperty("java.awt.headless", "false");
               System.setProperty("spring.main.web-application-type", "none");

               // Launch JavaFX application
               Application.launch(EmpSalaryMain.class, args);
           }
       }


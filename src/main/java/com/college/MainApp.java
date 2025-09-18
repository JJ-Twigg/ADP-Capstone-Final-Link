
package com.college;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MainApp {

    private static ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        // Start Spring Boot and block until it's ready
        springContext = SpringApplication.run(MainApp.class, args);

        // Once Spring Boot is ready, start JavaFX
        Application.launch(AppGuest.class, args);

    }

    public static ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }
}


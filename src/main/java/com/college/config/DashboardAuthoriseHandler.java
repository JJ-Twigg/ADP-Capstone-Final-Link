package com.college.config;

import com.college.MainFinal;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DashboardAuthoriseHandler {

    public void redirectToDashboard(Stage stage, Authentication auth) {
        String fxmlToLoad = "scenes/default-dashboard.fxml"; // default fallback

        for (GrantedAuthority authority : auth.getAuthorities()) {
            String role = authority.getAuthority();
            if (role.equals("ROLE_ADMIN")) {
                fxmlToLoad = "/scenes/dashboardAdmin.fxml";
                break;
            } else if (role.equals("ROLE_MANAGER")) {
                fxmlToLoad = "/scenes/dashboard.fxml";
                break;
            }
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlToLoad));
            loader.setControllerFactory(clz -> MainFinal.getSpringContext().getBean(clz));
            Parent dashboardRoot = loader.load();

            Scene scene = new Scene(dashboardRoot);
            stage.setScene(scene);
            stage.setWidth(1100);
            stage.setHeight(600);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

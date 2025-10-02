package com.college.config;

import com.college.MainFinal;
import com.college.controller.DashboardController;
import com.college.domain.User;
import com.college.repository.UserRepository;
import com.college.service.UserService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;


@Component
public class DashboardAuthoriseHandler {

    @Autowired
    UserRepository userRepository;

    public void redirectToDashboard(Stage stage, Authentication auth) {
        String fxmlToLoad = "scenes/default-dashboard.fxml"; // default fallback
        String role = "UNKNOWN";

        for (GrantedAuthority authority : auth.getAuthorities()) {
            role = authority.getAuthority();
            if (role.equals("ROLE_ADMIN")) {
                fxmlToLoad = "/scenes/dashboardAdmin.fxml";
                break;
            } else if (role.equals("ROLE_MANAGER")) {
                fxmlToLoad = "/scenes/dashboard.fxml";
                break;
            }else if (role.equals("ROLE_USER")) {  // <-- add this
                fxmlToLoad = "/scenes/dashboardUser.fxml";
                break;
            }
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlToLoad));
            loader.setControllerFactory(clz -> MainFinal.getSpringContext().getBean(clz));
            Parent dashboardRoot = loader.load();

            String email = auth.getName();

            Optional<User> userOpt = userRepository.findByEmail(email);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                String name = user.getName();
                String surname = user.getSurname();
                System.out.println("User full name: " + name + " " + surname);
            } else {
                System.out.println("User not found for email: " + email);
            }

            //use setter to pass email and role to dashboard page
            DashboardController controller = loader.getController();
            controller.setUserInfo(auth.getName(), role);

            if (userOpt.isPresent()) {
                controller.setName(userOpt.get().getName()); // pass the name to dashboardC then to overviewC using setter
            } else {
                controller.setName("Unknown");
            }

            Scene scene = new Scene(dashboardRoot);
            stage.setScene(scene);

            //DASHBOARD SIZING HERE
            stage.setWidth(1220);
            stage.setHeight(600);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

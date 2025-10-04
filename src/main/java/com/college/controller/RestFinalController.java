package com.college.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.springframework.stereotype.Controller;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.Base64;

@Controller
public class RestFinalController {

    @FXML
    private TextArea txtResponse;

    private final String BASE_URL = "http://localhost:8080/api";

    // Use the dashboard credentials
    private final String DASHBOARD_USER = "o";  // must bea DB email and pass
    private final String DASHBOARD_PASS = "o";

    @FXML
    public void callUserHello() {
        callRest(BASE_URL + "/user/hello");
    }

    @FXML
    public void callManagerHello() {
        callRest(BASE_URL + "/manager/hello");
    }

    @FXML
    public void callAdminHello() {
        callRest(BASE_URL + "/admin/hello");
    }

    private void callRest(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Encode username:password in Base64
            String auth = DASHBOARD_USER + ":" + DASHBOARD_PASS;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            String authHeader = "Basic " + encodedAuth;

            // Build request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", authHeader)
                    .GET()
                    .build();

            // Send request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            txtResponse.setText(response.body());

        } catch (Exception e) {
            txtResponse.setText("Error: " + e.getMessage());
        }
    }
}

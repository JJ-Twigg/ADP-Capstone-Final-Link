package com.college.service;

import com.college.domain.Event;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;

@Deprecated
public class EventUIService {

    private static final String BASE_URL = "http://localhost:8080/event";
    private final ObjectMapper objectMapper = new ObjectMapper();

    // CREATE
    public Event addEvent(Event event) throws Exception {
        String endpoint = BASE_URL + "/create";
        String json = objectMapper.writeValueAsString(event);

        HttpURLConnection conn = setupConnection(endpoint, "POST");
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
        }

        checkResponse(conn);
        return objectMapper.readValue(conn.getInputStream(), Event.class);
    }

    // READ
    public Event getEvent(int eventId) throws Exception {
        String endpoint = BASE_URL + "/read/" + eventId;
        HttpURLConnection conn = setupConnection(endpoint, "GET");
        checkResponse(conn);
        return objectMapper.readValue(conn.getInputStream(), Event.class);
    }

    // UPDATE
    public Event updateEvent(Event event) throws Exception {
        String endpoint = BASE_URL + "/update";
        String json = objectMapper.writeValueAsString(event);

        HttpURLConnection conn = setupConnection(endpoint, "PUT");
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
        }

        checkResponse(conn);
        return objectMapper.readValue(conn.getInputStream(), Event.class);
    }

    // DELETE
    public boolean deleteEvent(int eventId) throws Exception {
        String endpoint = BASE_URL + "/delete/" + eventId;
        HttpURLConnection conn = setupConnection(endpoint, "DELETE");
        checkResponse(conn);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            return Boolean.parseBoolean(br.readLine());
        }
    }

    // GET ALL
    public List<Event> getAllEvents() throws Exception {
        String endpoint = BASE_URL + "/getall";
        HttpURLConnection conn = setupConnection(endpoint, "GET");
        checkResponse(conn);
        return objectMapper.readValue(conn.getInputStream(), new TypeReference<List<Event>>() {});
    }

    // HELPERS
    private HttpURLConnection setupConnection(String endpoint, String method) throws Exception {
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");

        // ADD THIS: Basic Auth
        String userCredentials = "user:123"; // your Spring Security username and password
        String basicAuth = "Basic " + Base64.getEncoder().encodeToString(userCredentials.getBytes());
        conn.setRequestProperty("Authorization", basicAuth);

        conn.setDoOutput(true);
        return conn;
    }

    private void checkResponse(HttpURLConnection conn) throws Exception {
        int status = conn.getResponseCode();
        if (status >= 400) {
            throw new RuntimeException("HTTP request failed with code: " + status);
        }
    }
}
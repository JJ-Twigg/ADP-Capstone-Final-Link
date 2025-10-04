package com.college.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestfulWebservice {

    @GetMapping("/api/user/hello")
    public String userHello() {
        return "Hello USER (or higher up)";
    }

    @GetMapping("/api/manager/hello")
    public String managerHello() {
        return "Hello MANAGER (or ADMIN)";
    }

    @GetMapping("/api/admin/hello")
    public String adminHello() {
        return "Hello ADMIN only!";
    }
}

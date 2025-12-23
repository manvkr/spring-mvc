package com.example.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")  // Maps to root URL
    public String home() {
        return "home";  // Name of the JSP file (home.jsp)
    }
}


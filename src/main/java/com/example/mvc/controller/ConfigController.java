package com.example.mvc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class ConfigController {

    @GetMapping("/config")
    public Map<String,String> config(HttpServletRequest request){
        String base = System.getenv("APP_API_BASE_URL");
        if(base == null) base = System.getProperty("APP_API_BASE_URL");
        if(base == null || base.isBlank()){
            String schema = request.getScheme();
            String host = request.getServerName();
            int port = request.getServerPort();
            boolean standard = ("http".equalsIgnoreCase(schema) && port == 80)
                    ||  ("https".equalsIgnoreCase(schema) && port == 443);
            String ctx = request.getContextPath();
            base = schema + "://" + host + (standard ? "" : ":" + port) + ctx;
        }
        return Map.of("apiBaseUrl", base);
    }
}

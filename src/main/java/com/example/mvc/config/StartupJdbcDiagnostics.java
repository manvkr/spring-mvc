package com.example.mvc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Simple diagnostic logger to confirm JDBC properties are loaded as expected.
 * Remove or disable in production.
 */
@Component
public class StartupJdbcDiagnostics implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Mask password except first/last char for security
        String masked = password == null ? "null" : password.length() <= 2 ? "**" : (password.charAt(0) + "***" + password.charAt(password.length() - 1));
        System.out.println("[JDBC-DIAG] url=" + url);
        System.out.println("[JDBC-DIAG] username=" + username);
        System.out.println("[JDBC-DIAG] password(masked)=" + masked);
    }
}
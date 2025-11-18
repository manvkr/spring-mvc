package com.example.mvc.controller;

import com.example.mvc.model.Severity;
import com.example.mvc.model.Status;
import com.example.mvc.model.Bug;
import com.example.mvc.service.BugService;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BugController {

    private final BugService bugService;

    public BugController(BugService bugService) {
        this.bugService = bugService;
    }

    @GetMapping("/")
    public String index() {
        return "index"; // JSP view name
    }

    @GetMapping(value = "/bugs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Bug> listBugs(@RequestParam(value = "severity", required = false) Severity severity) {
        if (severity != null) {
            return bugService.getBugsBySeverity(severity);
        }
        return bugService.getAllBugs();
    }

    @PostMapping(value = "/bugs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Bug createBug(@RequestParam("bugTitle") String bugTitle,
                         @RequestParam("description") String description,
                         @RequestParam("status") Status status,
                         @RequestParam("severity") Severity severity) {
        return bugService.createBug(bugTitle, description, status, severity);
    }

    // Basic centralized exception to return JSON rather than HTML error page
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<?> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorPayload("Server error: " + ex.getMessage()));
    }

    // Simple DTO for error responses
    static class ErrorPayload {
        private final String message;
        public ErrorPayload(String message) { this.message = message; }
        public String getMessage() { return message; }
    }
}

package com.example.mvc.service;

import com.example.mvc.model.Severity;
import com.example.mvc.model.Status;
import com.example.mvc.model.Bug;

import java.util.List;

public interface BugService {
    List<Bug> getAllBugs();
    List<Bug> getBugsBySeverity(Severity severity);
    //List<Bug> getBugsByStatus(Status status);
    Bug createBug(String bugTitle, String description, Status status, Severity severity);
}
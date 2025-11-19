package com.example.mvc.service;

import com.example.mvc.exception.InvalidInputException;
import com.example.mvc.model.Severity;
import com.example.mvc.model.Status;
import com.example.mvc.model.Bug;
import com.example.mvc.repository.BugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BugService {

    @Autowired
    private BugRepository bugRepository;

    @Transactional(readOnly = true)
    public List<Bug> getAllBugs() {
        return bugRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Bug> getBugsBySeverity(Severity severity) {
        return bugRepository.findBySeverity(severity);
    }

    @Transactional
    public Bug createBug(String bugTitle, String description, Status status, Severity severity) {
        if (bugTitle == null || bugTitle.isEmpty()) {
            throw new InvalidInputException("Bug title cannot be empty");
        }

        Bug bug = new Bug();
        bug.setBugTitle(bugTitle);
        bug.setDescription(description);
        bug.setStatus(status);
        bug.setSeverity(severity);

        try {
            return bugRepository.save(bug);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save bug", e);
        }
    }
}
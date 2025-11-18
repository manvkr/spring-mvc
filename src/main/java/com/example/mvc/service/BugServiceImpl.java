package com.example.mvc.service;

import com.example.mvc.model.Severity;
import com.example.mvc.model.Status;
import com.example.mvc.model.Bug;
import com.example.mvc.repository.BugRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BugServiceImpl implements BugService {

    private final BugRepository bugRepository;

    public BugServiceImpl(BugRepository bugRepository) {
        this.bugRepository = bugRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bug> getAllBugs() {
        return bugRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bug> getBugsBySeverity(Severity severity) {
        return bugRepository.findBySeverity(severity);
    }

    @Override
    @Transactional
    public Bug createBug(String bugTitle, String description, Status status, Severity severity) {
        Bug bug = new Bug();
        bug.setBugTitle(bugTitle);
        bug.setDescription(description);
        bug.setStatus(status);
        bug.setSeverity(severity);
        return bugRepository.save(bug);
    }
}
package com.example.mvc.repository;

import com.example.mvc.model.Severity;
import com.example.mvc.model.Bug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BugRepository extends JpaRepository<Bug, Long> {

    List<Bug> findBySeverity(Severity severity);

}
package com.example.mvc.service;

import com.example.mvc.model.Bug;
import com.example.mvc.model.Severity;
import com.example.mvc.model.Status;
import com.example.mvc.repository.BugRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BugServiceTest {

    @Mock
    private BugRepository bugRepository;

    @InjectMocks
    private BugService bugService;

    private Bug bug1;
    private Bug bug2;

    @BeforeEach
    void setUp() {
        // Initialize test Bug objects
        bug1 = new Bug();
        bug1.setBugTitle("Bug1");
        bug1.setDescription("Desc1");
        bug1.setStatus(Status.OPEN);
        bug1.setSeverity(Severity.HIGH);

        bug2 = new Bug();
        bug2.setBugTitle("Bug2");
        bug2.setDescription("Desc2");
        bug2.setStatus(Status.CLOSED);
        bug2.setSeverity(Severity.LOW);
    }

    @Test
    void testGetAllBugs() {
        List<Bug> mockBugs = Arrays.asList(bug1, bug2);
        when(bugRepository.findAll()).thenReturn(mockBugs);

        List<Bug> result = bugService.getAllBugs();

        assertEquals(2, result.size());
        assertEquals("Bug1", result.get(0).getBugTitle());
        assertEquals("Bug2", result.get(1).getBugTitle());
        verify(bugRepository, times(1)).findAll();
    }

    @Test
    void testGetBugsBySeverity() {
        List<Bug> mockBugs = List.of(bug1);
        when(bugRepository.findBySeverity(Severity.HIGH)).thenReturn(mockBugs);

        List<Bug> result = bugService.getBugsBySeverity(Severity.HIGH);

        assertEquals(1, result.size());
        assertEquals(Severity.HIGH, result.get(0).getSeverity());
        verify(bugRepository, times(1)).findBySeverity(Severity.HIGH);
    }

    @Test
    void testCreateBug() {
        Bug newBug = new Bug();
        newBug.setBugTitle("Bug3");
        newBug.setDescription("Desc3");
        newBug.setStatus(Status.OPEN);
        newBug.setSeverity(Severity.MEDIUM);

        when(bugRepository.save(any(Bug.class))).thenReturn(newBug);

        Bug result = bugService.createBug("Bug3", "Desc3", Status.OPEN, Severity.MEDIUM);

        assertNotNull(result);
        assertEquals("Bug3", result.getBugTitle());
        assertEquals(Status.OPEN, result.getStatus());
        assertEquals(Severity.MEDIUM, result.getSeverity());
        verify(bugRepository, times(1)).save(any(Bug.class));
    }

    @Test
    void testGetAllBugs_whenRepositoryThrowsException() {
        when(bugRepository.findAll()).thenThrow(new RuntimeException("DB failure"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bugService.getAllBugs());

        assertEquals(ex.getMessage(),"DB failure");
    }

    // --------------------------------------------------------------------
    // findBySeverity(null) throws exception
    // --------------------------------------------------------------------
    @Test
    void testGetBugsBySeverity_whenSeverityIsNull() {
        when(bugRepository.findBySeverity(null))
                .thenThrow(new IllegalArgumentException("Severity cannot be null"));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> bugService.getBugsBySeverity(null)
        );

        assertEquals("Severity cannot be null", ex.getMessage());
    }


    // --------------------------------------------------------------------
    // createBug() - repository throws exception
    // --------------------------------------------------------------------
    @Test
    void testCreateBug_whenRepositoryThrowsException() {
        when(bugRepository.save(any(Bug.class)))
                .thenThrow(new RuntimeException("DB insert error"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> bugService.createBug("Bug A", "desc", Status.OPEN, Severity.HIGH)
        );

        assertEquals("Failed to save bug", ex.getMessage());
    }
}

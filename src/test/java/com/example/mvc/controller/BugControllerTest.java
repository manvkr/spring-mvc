package com.example.mvc.controller;

import com.example.mvc.model.Bug;
import com.example.mvc.model.Severity;
import com.example.mvc.model.Status;
import com.example.mvc.service.BugService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BugControllerTest {

    @Mock
    private BugService bugService;

    @InjectMocks
    private BugController bugController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bugController)
                .setControllerAdvice(bugController) // to test exception handler
                .build();
    }

    @Test
    void testIndex_returnsIndexView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void testListBugs_returnsAllBugs_whenSeverityIsNull() throws Exception {
        List<Bug> mockBugs = Arrays.asList(
                new Bug(1L, "Bug1", "Description1", Status.OPEN, Severity.HIGH),
                new Bug(2L, "Bug2", "Description2", Status.CLOSED, Severity.LOW)
        );

        when(bugService.getAllBugs()).thenReturn(mockBugs);

        mockMvc.perform(get("/bugs").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(mockBugs.size()))
                .andExpect(jsonPath("$[0].bugTitle").value("Bug1"))
                .andExpect(jsonPath("$[1].bugTitle").value("Bug2"));

        verify(bugService, times(1)).getAllBugs();
        verifyNoMoreInteractions(bugService);
    }

    @Test
    void testListBugs_returnsFilteredBugs_whenSeverityProvided() throws Exception {
        List<Bug> mockBugs = List.of(
                new Bug(1L, "Bug1", "Description1", Status.OPEN, Severity.HIGH)
        );

        when(bugService.getBugsBySeverity(Severity.HIGH)).thenReturn(mockBugs);

        mockMvc.perform(get("/bugs")
                        .param("severity", "HIGH")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(mockBugs.size()))
                .andExpect(jsonPath("$[0].severity").value("HIGH"));

        verify(bugService, times(1)).getBugsBySeverity(Severity.HIGH);
        verifyNoMoreInteractions(bugService);
    }

    @Test
    void testCreateBug_returnsCreatedBug() throws Exception {
        Bug newBug = new Bug(1L, "Bug1", "Desc", Status.OPEN, Severity.MEDIUM);
        when(bugService.createBug("Bug1", "Desc", Status.OPEN, Severity.MEDIUM)).thenReturn(newBug);

        mockMvc.perform(post("/bugs")
                        .param("bugTitle", "Bug1")
                        .param("description", "Desc")
                        .param("status", "OPEN")
                        .param("severity", "MEDIUM")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bugTitle").value("Bug1"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.severity").value("MEDIUM"));

        verify(bugService, times(1)).createBug("Bug1", "Desc", Status.OPEN, Severity.MEDIUM);
        verifyNoMoreInteractions(bugService);
    }

    @Test
    void testHandleException_returnsInternalServerError() throws Exception {
        Exception ex = new Exception("Something went wrong");

        ResponseEntity<?> response = bugController.handleException(ex);

        assert response.getStatusCodeValue() == 500;
        assert ((BugController.ErrorPayload) response.getBody()).getMessage()
                .contains("Something went wrong");
    }
}
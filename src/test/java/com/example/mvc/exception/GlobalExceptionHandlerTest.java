package com.example.mvc.exception;

import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    @Test
    void notFoundMappingProduces404() {
        GlobalExceptionHandler h = new GlobalExceptionHandler();
        MockHttpServletRequest req = new MockHttpServletRequest("GET","/users/99");
        var response = h.handleNotFound(new NotFoundException("User id 99 not found"), new ServletWebRequest(req));
        assertEquals(404, response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains("99"));
    }
}

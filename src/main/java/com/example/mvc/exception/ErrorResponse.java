package com.example.mvc.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ErrorResponse {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private String path;
    private String message;
    private String error;
    private int status;

    public ErrorResponse(LocalDateTime timestamp, String path, String message, String error, int status) {
        this.timestamp = timestamp;
        this.path = path;
        this.message = message;
        this.error = error;
        this.status = status;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public String getPath() { return path; }
    public String getMessage() { return message; }
    public String getError() { return error; }
    public int getStatus() { return status; }
}

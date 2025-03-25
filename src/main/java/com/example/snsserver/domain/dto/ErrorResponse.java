package com.example.snsserver.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String message;
    private String details;
    private int status;

    public ErrorResponse(String message, String details, int status) {
        this.message = message;
        this.details = details;
        this.status = status;
    }
}
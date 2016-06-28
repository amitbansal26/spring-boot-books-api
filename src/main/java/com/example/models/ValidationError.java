package com.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidationError {
    private String message;

    public ValidationError(String message) {
        this.message = message;
    }

    @JsonProperty("error")
    public String getMessage() {
        return message;
    }
}

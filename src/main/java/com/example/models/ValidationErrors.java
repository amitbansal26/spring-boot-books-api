package com.example.models;

import java.util.Collection;

public class ValidationErrors {
    private Collection<ValidationError> errors;

    public ValidationErrors(Collection<ValidationError> errors) {
        this.errors = errors;
    }

    public Collection<ValidationError> getErrors() {
        return errors;
    }
}

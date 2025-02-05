package com.github.renas.Exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);  // Pass the message to the parent class (RuntimeException)
    }
}
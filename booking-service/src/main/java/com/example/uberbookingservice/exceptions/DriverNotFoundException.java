package com.example.uberbookingservice.exceptions;

public class DriverNotFoundException extends ResourceNotFoundException {
    public DriverNotFoundException(String message) {
        super(message);
    }
}

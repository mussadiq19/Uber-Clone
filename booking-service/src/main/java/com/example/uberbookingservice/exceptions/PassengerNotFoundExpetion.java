package com.example.uberbookingservice.exceptions;

public class PassengerNotFoundExpetion extends ResourceNotFoundException {
    public PassengerNotFoundExpetion(String message) {
        super(message);
    }
}

package com.example.uberbookingservice.exceptions;

public class BookingNotFoundException extends ResourceNotFoundException {
    public BookingNotFoundException(String message){
        super(message);
    }
}

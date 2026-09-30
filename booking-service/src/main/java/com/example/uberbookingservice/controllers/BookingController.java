package com.example.uberbookingservice.controllers;

import com.example.uberbookingservice.dtos.CreateBookingDto;
import com.example.uberbookingservice.dtos.CreateBookingResponseDto;
import com.example.uberbookingservice.dtos.UpdateBookingRequestDto;
import com.example.uberbookingservice.dtos.UpdateBookingResponseDto;
import com.example.uberbookingservice.services.BookingServices;
import jakarta.ws.rs.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {
    private final BookingServices bookingServices;

    public BookingController(BookingServices bookingServices) {
        this.bookingServices = bookingServices;
    }

    @PostMapping
    private ResponseEntity<CreateBookingResponseDto> createBooking(@RequestBody CreateBookingDto createBookingDto){
        return new ResponseEntity<>(bookingServices.createBooking(createBookingDto), HttpStatus.OK);

    }
    @PostMapping("/{bookingId}")
    public ResponseEntity<UpdateBookingResponseDto>updateBooking(@RequestBody UpdateBookingRequestDto requestDto,@PathVariable Long bookingId){
        return new ResponseEntity<>(bookingServices.updateBooking(requestDto,bookingId),HttpStatus.OK);

    }
    @GetMapping("/{bookingId}")
    public ResponseEntity<UpdateBookingResponseDto>getBooking(@PathVariable Long bookingId){
        return new ResponseEntity<>(bookingServices.getBooking(bookingId),HttpStatus.OK);

    }
}

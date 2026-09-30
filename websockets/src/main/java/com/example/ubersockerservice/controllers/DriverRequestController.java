package com.example.ubersockerservice.controllers;

import com.example.ubersockerservice.apis.BookingServiceApi;
import com.example.ubersockerservice.dtos.RideRequestDto;
import com.example.ubersockerservice.dtos.RideResponseDto;
import com.example.ubersockerservice.dtos.UpdateBookingRequestDto;
import com.example.ubersockerservice.dtos.UpdateBookingResponseDto;
import com.example.ubersockerservice.producers.KafkaProducerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/socket")
public class DriverRequestController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RestTemplate restTemplate;
    private final KafkaProducerService kafkaProducerService;
    private final BookingServiceApi bookingServiceApi;

    public DriverRequestController(SimpMessagingTemplate simpMessagingTemplate, KafkaProducerService kafkaProducerService, BookingServiceApi bookingServiceApi) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.kafkaProducerService = kafkaProducerService;
        this.bookingServiceApi = bookingServiceApi;
        this.restTemplate = new RestTemplate();
    }
    @GetMapping
    public Boolean help(){
        kafkaProducerService.publishMessage("sample-topic","hello1");
        return true;
    }

    @PostMapping("/newride")
    public ResponseEntity<Boolean> raiseRideRequest(@RequestBody RideRequestDto requestDto){
        sendDriverNewRideRequest(requestDto);
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }
    public void sendDriverNewRideRequest(RideRequestDto requestDto){
        simpMessagingTemplate.convertAndSend( "/topic/rideRequest",requestDto);
    }
    @MessageMapping("/rideResponse/{userId}")
    public synchronized void rideResponseHandler(@DestinationVariable String userId, RideResponseDto rideResponseDto){
        System.out.println(rideResponseDto.getResponse()+" "+ userId);
        UpdateBookingRequestDto requestDto = UpdateBookingRequestDto.builder()
                .driverId(Long.parseLong(userId))
                .bookingStatus("SCHEDULED")
                .build();
        try {
            bookingServiceApi.updateBooking(requestDto,rideResponseDto.bookingId).execute();
        }catch (Exception e){
//            System.out.println("Failed to update Booking"+ e.getMessage());
            e.printStackTrace();
        }
    }
}

package com.example.uberbookingservice.dtos;

import com.example.uberprojectentityservice.models.Driver;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CreateBookingResponseDto {
    private Long bookingId;

    private String bookingStatus;

    private Optional<Driver>driver;
}

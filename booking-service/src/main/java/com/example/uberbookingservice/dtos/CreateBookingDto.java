package com.example.uberbookingservice.dtos;

import com.example.uberprojectentityservice.models.ExactLocation;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingDto {
    private Long passengerId;

    private ExactLocation startLocation;

    private ExactLocation endLocation;
}

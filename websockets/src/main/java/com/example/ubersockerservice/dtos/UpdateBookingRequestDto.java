package com.example.ubersockerservice.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookingRequestDto {
    private String bookingStatus;
    private Long driverId;
}

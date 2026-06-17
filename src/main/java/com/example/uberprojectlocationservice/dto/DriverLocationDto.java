package com.example.uberprojectlocationservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverLocationDto {
    String driverId;
    Double latitude;
    Double longitude;

}

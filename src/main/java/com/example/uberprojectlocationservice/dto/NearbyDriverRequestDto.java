package com.example.uberprojectlocationservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NearbyDriverRequestDto {
    Double latitude;
    Double longitude;
}

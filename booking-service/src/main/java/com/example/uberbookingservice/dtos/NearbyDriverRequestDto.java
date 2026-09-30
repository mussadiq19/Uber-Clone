package com.example.uberbookingservice.dtos;

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

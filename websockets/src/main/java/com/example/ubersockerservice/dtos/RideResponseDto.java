package com.example.ubersockerservice.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideResponseDto {
    public Boolean response;
    public Long bookingId;

}

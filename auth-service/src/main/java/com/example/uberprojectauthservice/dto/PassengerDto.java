package com.example.uberprojectauthservice.dto;

import com.example.uberprojectentityservice.models.Passenger;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDto {
    private String id;
    private String email;

    private String name;

    private String phoneNumber;

    private String password;

    private LocalDateTime   createdAt;

    public static  PassengerDto from(Passenger p){
        return PassengerDto.builder()
                .id(p.getId().toString())
                .createdAt(p.getCreatedAt())
                .password(p.getPassword())
                .name(p.getName())
                .phoneNumber(p.getPhoneNumber())
                .email(p.getEmail())
                .build();
    }
}

package com.example.uberprojectauthservice.dto;

import com.example.uberprojectentityservice.models.Driver;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverDto {
    private String id;
    private String email;

    private String name;

    private String phoneNumber;

    private String password;

    private LocalDateTime createdAt;

    public static  DriverDto from(Driver d){
        return DriverDto.builder()
                .id(d.getId().toString())
                .createdAt(d.getCreatedAt())
                .password(d.getPassword())
                .name(d.getName())
                .phoneNumber(d.getPhNo())
                .email(d.getEmail())
                .build();
    }
}

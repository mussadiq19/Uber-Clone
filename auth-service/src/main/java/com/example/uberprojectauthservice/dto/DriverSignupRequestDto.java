package com.example.uberprojectauthservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverSignupRequestDto {

    private String email;

    private String name;

    private String phoneNumber;

    private String password;

}

package com.example.uberprojectauthservice.services;

import com.example.uberprojectauthservice.dto.DriverDto;
import com.example.uberprojectauthservice.dto.DriverSignupRequestDto;
import com.example.uberprojectauthservice.dto.PassengerDto;
import com.example.uberprojectauthservice.dto.PassengerSignupRequestDto;
import com.example.uberprojectauthservice.repositories.DriverRepository;
import com.example.uberprojectauthservice.repositories.PassengerRepository;
import com.example.uberprojectentityservice.models.Driver;
import com.example.uberprojectentityservice.models.Passenger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final PassengerRepository passengerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DriverRepository driverRepository;

    public AuthService(PassengerRepository passengerRepository, BCryptPasswordEncoder bCryptPasswordEncoder, DriverRepository driverRepository) {
        this.passengerRepository = passengerRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.driverRepository = driverRepository;
    }

    public PassengerDto signupPassenger(PassengerSignupRequestDto passengerSignupRequestDto){
        Passenger passenger =Passenger.builder()
                .email(passengerSignupRequestDto.getEmail())
                .phoneNumber(passengerSignupRequestDto.getPhoneNumber())
                .password(bCryptPasswordEncoder.encode( passengerSignupRequestDto.getPassword()))
                .name(passengerSignupRequestDto.getName())
                .build();

               Passenger newPassenger= passengerRepository.save(passenger);
               return(PassengerDto.from(newPassenger));
     }
     public DriverDto signupDriver(DriverSignupRequestDto driverSignupRequestDto){
        Driver driver = Driver.builder()
                .email(driverSignupRequestDto.getEmail())
                .phNo(driverSignupRequestDto.getPhoneNumber())
                .password(bCryptPasswordEncoder.encode(driverSignupRequestDto.getPassword()))
                .name(driverSignupRequestDto.getName())
                .build();
        Driver newDriver = driverRepository.save(driver);
        return(DriverDto.from(newDriver));
     }
}

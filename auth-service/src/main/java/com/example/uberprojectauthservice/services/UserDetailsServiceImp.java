package com.example.uberprojectauthservice.services;

import com.example.uberprojectauthservice.helper.AuthDriverDetails;
import com.example.uberprojectauthservice.helper.AuthPassengerDetails;
import com.example.uberprojectauthservice.repositories.DriverRepository;
import com.example.uberprojectauthservice.repositories.PassengerRepository;
import com.example.uberprojectentityservice.models.Driver;
import com.example.uberprojectentityservice.models.Passenger;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final PassengerRepository passengerRepository;
    private final DriverRepository driverRepository;

    public UserDetailsServiceImp(PassengerRepository passengerRepository, DriverRepository driverRepository) {
        this.passengerRepository = passengerRepository;
        this.driverRepository = driverRepository;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        Optional<Passenger> passenger=passengerRepository.findPassengerByEmail(email);
        if(passenger.isPresent()) {
            return new AuthPassengerDetails(passenger.get());
        }
        Optional<Driver> driver = driverRepository.findDriverByEmail(email);
        if (driver.isPresent()) {
            return new AuthDriverDetails(driver.get());
        }
        throw new UsernameNotFoundException("cannot find the passenger or driver by the given email");

    }
}


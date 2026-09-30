package com.example.uberprojectlocationservice.services;

import com.example.uberprojectlocationservice.dto.DriverLocationDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationService {
    Boolean saveDriverLocation(String driverId,Double latitude,Double longitude);
    List<DriverLocationDto>getNearbyDrivers(Double latitude,Double longitude);
}

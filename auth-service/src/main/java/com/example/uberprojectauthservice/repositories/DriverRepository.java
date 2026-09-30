package com.example.uberprojectauthservice.repositories;

import com.example.uberprojectentityservice.models.Driver;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver,Long> {
    Optional<Driver> findDriverByEmail(@NonNull String email);
}

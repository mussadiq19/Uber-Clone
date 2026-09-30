package com.example.uberreviewservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Passenger extends BaseModel {

    public String name;

    @OneToMany(mappedBy = "passenger")
    private List<Booking>bookings = new ArrayList<>();
}
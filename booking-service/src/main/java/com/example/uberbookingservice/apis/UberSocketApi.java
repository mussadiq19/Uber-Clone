package com.example.uberbookingservice.apis;

import com.example.uberbookingservice.dtos.DriverLocationDto;
import com.example.uberbookingservice.dtos.NearbyDriverRequestDto;
import com.example.uberbookingservice.dtos.RideRequestDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UberSocketApi {
    @POST("api/socket/newride")
    Call<Boolean> raiseRideRequest(@Body RideRequestDto requestDto);
}

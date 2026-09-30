package com.example.ubersockerservice.apis;

import com.example.ubersockerservice.dtos.UpdateBookingRequestDto;
import com.example.ubersockerservice.dtos.UpdateBookingResponseDto;
import jakarta.ws.rs.GET;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface BookingServiceApi {
    @POST("api/v1/booking/{bookingId}")
    Call<UpdateBookingResponseDto>updateBooking(@Body UpdateBookingRequestDto requestDto, @Path("bookingId") Long bookingId);
}

package com.example.uberbookingservice.services;

import com.example.uberbookingservice.apis.LocationServiceApi;
import com.example.uberbookingservice.apis.UberSocketApi;
import com.example.uberbookingservice.dtos.*;
import com.example.uberbookingservice.exceptions.BookingNotFoundException;
import com.example.uberbookingservice.repositories.BookingRepository;
import com.example.uberbookingservice.repositories.DriverRepository;
import com.example.uberbookingservice.repositories.PassengerRepository;
import com.example.uberprojectentityservice.models.Booking;
import com.example.uberprojectentityservice.models.BookingStatus;
import com.example.uberprojectentityservice.models.Driver;
import com.example.uberprojectentityservice.models.Passenger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingServices  {


        private final PassengerRepository passengerRepository;
        private final BookingRepository bookingRepository;
        private final RestTemplate restTemplate;
        private final LocationServiceApi locationServiceApi;

        private static  final String LOCATION_SERVICE="http://localhost:7777";
        private final DriverRepository driverRepository;
        private final UberSocketApi uberSocketApi;

    public BookingServiceImpl(PassengerRepository passengerRepository, BookingRepository bookingRepository, LocationServiceApi locationServiceApi, DriverRepository driverRepository, UberSocketApi uberSocketApi) {
                this.passengerRepository = passengerRepository;
                this.bookingRepository = bookingRepository;
            this.locationServiceApi = locationServiceApi;
        this.uberSocketApi = uberSocketApi;
        this.restTemplate = new RestTemplate();
        this.driverRepository = driverRepository;
    }

        @Override
        public CreateBookingResponseDto createBooking(CreateBookingDto bookingDetails) {
                Optional<Passenger> passenger= passengerRepository.findById(bookingDetails.getPassengerId());
                Booking booking =Booking.builder()
                        .bookingStatus(BookingStatus.ASSIGNING_DRIVER)
                        .startLocation(bookingDetails.getStartLocation())
                        .endLocation(bookingDetails.getEndLocation())
                        .build();
               Booking newBooking= bookingRepository.save(booking);
         //make an api call to location service to fetch nearby drivers
                NearbyDriverRequestDto request = NearbyDriverRequestDto.builder()
                        .latitude(bookingDetails.getStartLocation().getLatitude())
                        .longitude(bookingDetails.getStartLocation().getLongitude())
                        .build();
                processNearbyDriversAsync(request,bookingDetails.getPassengerId(),newBooking.getId());
//                ResponseEntity<DriverLocationDto[]> result= restTemplate.postForEntity(LOCATION_SERVICE+"/api/location/nearby/drivers",request,DriverLocationDto[].class);
////try this with retrofit
//                if(result.getStatusCode().is2xxSuccessful() && result.getBody()!=null){
//                        List<DriverLocationDto> driverLocation = Arrays.asList(result.getBody());
//                        driverLocation.forEach(driverLocationDto -> {
//                                System.out.println(driverLocationDto.getDriverId()+" "+ " lat: "+driverLocationDto.getLatitude()+" "+"long:"+driverLocationDto.getLongitude());
//                        });
//                }

                return CreateBookingResponseDto.builder()
                        .bookingId(newBooking.getId())
                        .bookingStatus(newBooking.getBookingStatus().toString())
                        //.driver(Optional.of(newBooking.getDriver()))
                        .build();


        }

    @Override
    public UpdateBookingResponseDto updateBooking(UpdateBookingRequestDto bookingRequestDto, Long bookingId) {

       Driver driver = driverRepository.findById(bookingRequestDto.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found with id: " + bookingRequestDto.getDriverId()));
//        if(driver.getIsAvailable() && driver.is)
        bookingRepository.updateBookingStatusAndDriverById(bookingId, BookingStatus.SCHEDULED, driver);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + bookingId));

        return UpdateBookingResponseDto.builder()
                .bookingId(bookingId)
                .status(booking.getBookingStatus())
                .driver(Optional.ofNullable(booking.getDriver()))
                .build();
    }

    @Override
    public UpdateBookingResponseDto getBooking(Long bookingId) {
        Booking booking=bookingRepository.findById(bookingId)
                .orElseThrow(()->new BookingNotFoundException("Booking not found"));
        return UpdateBookingResponseDto.builder()
                .bookingId(booking.getId())
                .status(booking.getBookingStatus())
                .driver(Optional.ofNullable(booking.getDriver()))
                .build();
    }

    private void processNearbyDriversAsync(NearbyDriverRequestDto requestDto, Long passengerId, Long bookingId){
            Call<DriverLocationDto[]>call=locationServiceApi.getNearbyDrivers(requestDto);
            call.enqueue(new Callback<DriverLocationDto[]>() {
                @Override
                public void onResponse(Call<DriverLocationDto[]> call, Response<DriverLocationDto[]> response) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Location Service HTTP Code: " + response.code());
                    if(response.isSuccessful() && response.body()!=null){
                        List<DriverLocationDto> driverLocation = Arrays.asList(response.body());
                        driverLocation.forEach(driverLocationDto -> {
                                System.out.println(driverLocationDto.getDriverId()+" "
                                        + " lat: "+driverLocationDto.getLatitude()+" "
                                        +"long:"+driverLocationDto.getLongitude());
                        });
                        raiseRideRequestAsync(RideRequestDto.builder().passengerId(passengerId).bookingId(bookingId).build());
                    }else {
                        System.out.println("Location Service Request Failed");
                        System.out.println("HTTP Code: " + response.code());
                        System.out.println("Message: " + response.message());

                        try {
                            if (response.errorBody() != null) {
                                System.out.println(response.errorBody().string());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                 }
                }

                @Override
                public void onFailure(Call<DriverLocationDto[]> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        }
    private void raiseRideRequestAsync(RideRequestDto requestDto){
        Call<Boolean>call =uberSocketApi.raiseRideRequest(requestDto);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                System.out.println("Socket Service HTTP Code: " + response.code());
                if(response.isSuccessful() && response.body()!=null){

                    System.out.println("Ride request sent successfully.");
                    System.out.println("Response: " + response.body());

                    Boolean result =response.body();
                    System.out.println("Driver response is" +result.toString());
                }else {

                    System.out.println("Socket Service Request Failed");
                    System.out.println("HTTP Code: " + response.code());
                    System.out.println("Message: " + response.message());

                    try {
                        if (response.errorBody() != null) {
                            System.out.println(response.errorBody().string());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

}

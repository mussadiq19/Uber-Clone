package com.example.uberbookingservice.repositories;

import com.example.uberprojectentityservice.models.Booking;
import com.example.uberprojectentityservice.models.BookingStatus;
import com.example.uberprojectentityservice.models.Driver;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    @Modifying
    @Transactional
    @Query("update Booking b set b.bookingStatus = :bookingStatus,b.driver= :driver where b.id = :id")
    void updateBookingStatusAndDriverById(@Param("id")Long id, @Param("bookingStatus") BookingStatus bookingStatus, @Param("driver") Driver driver);
}

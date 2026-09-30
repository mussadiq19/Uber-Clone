package com.example.uberreviewservice.repositories;

import com.example.uberreviewservice.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DriverRepository extends JpaRepository <Driver,Long>{

    Optional<Driver> findByIdAndLicenceNumber(Long id, String licenceNumber);

    @Query(nativeQuery = true,value = "Select *FROM Driver WHERE id=:id AND license_number=:license")
    Optional<Driver> rawFindByIdAndLicenceNumber(Long id,String license); //Raw sql query error thrown at runtime


    @Query("SELECT d FROM Driver d WHERE d.id = :id AND d.licenceNumber=:licence") //Hibernate query,error thrown at compole time
    Optional<Driver> hqlFindByIdLicense(Long id,String licence);

    List<Driver> findAllByIdIn(List<Long>driverIds);
}

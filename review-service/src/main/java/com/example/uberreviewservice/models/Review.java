package com.example.uberreviewservice.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tools.jackson.core.ObjectReadContext;

import java.util.Date;
import java.util.Calendar;

@Entity  //this will tell the spring that this class is going to hold logical
        //representation of our data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="booking_review")
public class Review extends BaseModel {

    @Column(nullable = false)
    private String content;

    private Double rating;

    @OneToOne(cascade= {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Booking booking;//we have defined one-to-one relationship

    @Override
    public String toString(){

        return "Review:"+this.content+" "+this.rating+" "+"booking:"+this.booking.getId()+" "+this.createdAt;
    }




}

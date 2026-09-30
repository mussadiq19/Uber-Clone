package com.example.uberprojectentityservice.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@MappedSuperclass //handling inheritance in spring data jpa
@EntityListeners(AuditingEntityListener.class)
//This class has any bare minimum properties that any other class will be  requiring
public abstract class BaseModel {

    @Id  //this annotation make this is property the primary key of our table
    @GeneratedValue(strategy = GenerationType.IDENTITY)//IDENTITY means auto_increment
    protected Long id;


    @Column(nullable = false)
    @CreatedDate
    protected LocalDateTime createdAt;


    @Column(nullable = false)
    @LastModifiedDate
    protected LocalDateTime updatedAt;

}

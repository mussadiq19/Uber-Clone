package com.example.uberprojectentityservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DBConstants extends BaseModel{

    @Column(unique = true,nullable = false)
    private String name;

    private String Value;
}

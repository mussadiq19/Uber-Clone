package com.example.ubersockerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
@EntityScan("com.example.uberprojectentityservice.models")
public class UberSockerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UberSockerServiceApplication.class, args);
    }

}

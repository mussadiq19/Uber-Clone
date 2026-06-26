package com.example.uberservicediscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class UberServiceDIscoveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(UberServiceDIscoveryApplication.class, args);
    }

}

package com.parking.smart.sp_parking_scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpParkingSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpParkingSchedulerApplication.class, args);
    }

}

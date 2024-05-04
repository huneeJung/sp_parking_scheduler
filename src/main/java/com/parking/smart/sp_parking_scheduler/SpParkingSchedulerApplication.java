package com.parking.smart.sp_parking_scheduler;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Locale;
import java.util.TimeZone;

@Slf4j
@EnableScheduling
@SpringBootApplication
@RequiredArgsConstructor
public class SpParkingSchedulerApplication {

    private final Environment env;

    public static void main(String[] args) {
        SpringApplication.run(SpParkingSchedulerApplication.class, args);
    }

    @PostConstruct
    private void initialize() {
        Locale.setDefault(Locale.KOREA);
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        System.setProperty("aws.ec2.metadata.disabled", "true");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void applicationReadyEvent() {
        log.info("Application Name ::: {}", env.getProperty("spring.application.name"));
        log.info("Locale           ::: {}", Locale.getDefault());
        log.info("Thread Limit     ::: {}", env.getProperty("server.tomcat.threads.max"));
        log.info("Connection Limit ::: {}", env.getProperty("server.tomcat.max-connections"));
        log.info("Hikari Pool Size ::: {}", env.getProperty("spring.datasource.hikari.maximum-pool-size"));
    }

}

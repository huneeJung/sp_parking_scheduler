package com.parking.smart.sp_parking_scheduler.biz.parking.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParkingLotSyncScheduler {

    private final JobLauncher jobLauncher;

    @Qualifier("syncParkingLotJob")
    private final Job syncParkingLotJob;

    //    @Scheduled(cron = "0 0 3 * * *")

    @Scheduled(fixedDelay = 50000000, initialDelay = 5000)
    private void syncParkingLotInfo() {
        var start = Instant.now();
        Map<String, JobParameter> configMap = new HashMap<>();
        try {
            JobExecution jobExecution = jobLauncher.run(syncParkingLotJob, new JobParameters());
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException |
                 org.springframework.batch.core.repository.JobRestartException e) {

            log.warn(e.getMessage());
        }
        var end = Instant.now();
        long workingTime = Duration.between(start, end).toMillis();
        log.info("ParkingLot Synchronize End ::: workTime {}ms", workingTime);
    }

}

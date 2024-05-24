package com.parking.smart.sp_parking_scheduler.biz.parking.jobs;

import com.parking.smart.sp_parking_scheduler.biz.parking.tasklets.SyncParkingLotStepATasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SyncParkingLotJobConfig {

    private final SyncParkingLotStepATasklet syncParkingLotStepATasklet;

    @Bean
    public Job syncParkingLotJob(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new JobBuilder("syncParkingLotJob",jobRepository)
                .start(syncParkingLotStep(jobRepository,transactionManager))
                .build();
    }

    @Bean
    public Step syncParkingLotStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("step1",jobRepository)
                .tasklet(syncParkingLotStepATasklet,transactionManager)
                .build();
    }

}

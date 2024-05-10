package com.parking.smart.sp_parking_scheduler.biz.holiday.repository;

import com.parking.smart.sp_parking_scheduler.biz.holiday.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    Optional<Holiday> findByDate(String date);

}

package com.parking.smart.sp_parking_scheduler.biz.parking.repository;

import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {
}

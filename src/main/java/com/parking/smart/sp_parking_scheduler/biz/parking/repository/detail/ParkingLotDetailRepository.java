package com.parking.smart.sp_parking_scheduler.biz.parking.repository.detail;

import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLotDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingLotDetailRepository extends JpaRepository<ParkingLotDetail, Long> {
}

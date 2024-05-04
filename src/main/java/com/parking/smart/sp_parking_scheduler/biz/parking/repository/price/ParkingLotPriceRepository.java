package com.parking.smart.sp_parking_scheduler.biz.parking.repository.price;

import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLotPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingLotPriceRepository extends JpaRepository<ParkingLotPrice, String> {
}

package com.parking.smart.sp_parking_scheduler.biz.parking.repository.lot;

import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, String> {

    Set<ParkingLot> findByCodeIn(Set<String> codeList);

}


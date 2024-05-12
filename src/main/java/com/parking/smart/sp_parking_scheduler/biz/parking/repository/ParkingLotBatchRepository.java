package com.parking.smart.sp_parking_scheduler.biz.parking.repository;

import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLot;

import java.util.List;

public interface ParkingLotBatchRepository {

    void batchInsert(List<ParkingLot> batchList);

    void batchUpdate(List<ParkingLot> batchList);

}

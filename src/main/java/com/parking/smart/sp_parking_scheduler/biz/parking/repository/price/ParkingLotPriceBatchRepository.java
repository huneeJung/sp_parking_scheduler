package com.parking.smart.sp_parking_scheduler.biz.parking.repository.price;

import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLotPrice;

import java.util.List;

public interface ParkingLotPriceBatchRepository {

    void batchInsert(List<ParkingLotPrice> batchList);

    void batchUpdate(List<ParkingLotPrice> batchList);
    
}

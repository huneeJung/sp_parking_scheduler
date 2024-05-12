package com.parking.smart.sp_parking_scheduler.biz.parking.repository.detail;

import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLotDetail;

import java.util.List;

public interface ParkingLotDetailBatchRepository {

    void batchInsert(List<ParkingLotDetail> batchList);

    void batchUpdate(List<ParkingLotDetail> batchList);

}

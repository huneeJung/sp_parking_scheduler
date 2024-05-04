package com.parking.smart.sp_parking_scheduler.biz.parking.service;

import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLot;
import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLotDetail;
import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLotPrice;
import com.parking.smart.sp_parking_scheduler.biz.parking.model.ParkingInfo;
import com.parking.smart.sp_parking_scheduler.biz.parking.repository.detail.ParkingLotDetailBatchRepository;
import com.parking.smart.sp_parking_scheduler.biz.parking.repository.lot.ParkingLotBatchRepository;
import com.parking.smart.sp_parking_scheduler.biz.parking.repository.lot.ParkingLotRepository;
import com.parking.smart.sp_parking_scheduler.biz.parking.repository.price.ParkingLotPriceBatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ParkingLotService {
    private final ParkingLotRepository parkingLotRepository;

    private final ParkingLotBatchRepository parkingLotBatchRepository;
    private final ParkingLotDetailBatchRepository parkingLotDetailBatchRepository;
    private final ParkingLotPriceBatchRepository parkingLotPriceBatchRepository;

    public Set<ParkingLot> findAnyByCodes(Set<String> codes) {
        return parkingLotRepository.findByCodeIn(codes);
    }

    @Transactional
    public void insertParkingLot(ParkingInfo parkingInfo) {
        var id = parkingInfo.getCode();

        var insertParkingLot = parkingInfo.toParkingLot();
        insertParkingLot.setCode(id);

        var insertParkingLotDetail = parkingInfo.toParkingLotDetail();
        insertParkingLot.setParkingLotDetail(insertParkingLotDetail);

        var insertParkingLotPrice = parkingInfo.toParkingLotPrice();
        insertParkingLot.setParkingLotPrice(insertParkingLotPrice);

        parkingLotRepository.save(insertParkingLot);
    }

    @Transactional
    public void insertBatch(
            List<ParkingLot> updateParkingLotList, List<ParkingLotDetail> updateParkingLotDetailList,
            List<ParkingLotPrice> updateParkingLotPriceList
    ) {
        parkingLotBatchRepository.batchUpdate(updateParkingLotList);
        parkingLotDetailBatchRepository.batchUpdate(updateParkingLotDetailList);
        parkingLotPriceBatchRepository.batchUpdate(updateParkingLotPriceList);
    }

    @Transactional
    public void updateBatch(
            List<ParkingLot> insertParkingLotList, List<ParkingLotDetail> insertParkingLotDetailList,
            List<ParkingLotPrice> insertParkingLotPriceList
    ) {
        parkingLotBatchRepository.batchInsert(insertParkingLotList);
        parkingLotDetailBatchRepository.batchInsert(insertParkingLotDetailList);
        parkingLotPriceBatchRepository.batchInsert(insertParkingLotPriceList);
    }

}

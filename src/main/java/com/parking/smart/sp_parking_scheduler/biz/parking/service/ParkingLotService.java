package com.parking.smart.sp_parking_scheduler.biz.parking.service;

import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLot;
import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLotDetail;
import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLotPrice;
import com.parking.smart.sp_parking_scheduler.biz.parking.model.ParkingInfo;
import com.parking.smart.sp_parking_scheduler.biz.parking.repository.ParkingLotRepository;
import com.parking.smart.sp_parking_scheduler.biz.parking.repository.detail.ParkingLotDetailRepository;
import com.parking.smart.sp_parking_scheduler.biz.parking.repository.price.ParkingLotPriceRepository;
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

    // JDBC Batch Repo
    private final ParkingLotRepository parkingLotRepository;
    private final ParkingLotDetailRepository parkingLotDetailRepository;
    private final ParkingLotPriceRepository parkingLotPriceRepository;

    public List<ParkingLot> findAnyByCodes(Set<String> codes) {
        return parkingLotRepository.findByCodeIn(codes);
    }

    @Transactional
    public void insertParkingLot(ParkingInfo parkingInfo) {
        var id = parkingInfo.getCode();

        var insertParkingLot = parkingInfo.toParkingLot();
        insertParkingLot.setCode(id);

        var insertParkingLotDetail = parkingInfo.toParkingLotDetail();
        insertParkingLotDetail.setCode(id);
        insertParkingLot.setParkingLotDetail(insertParkingLotDetail);

        var insertParkingLotPrice = parkingInfo.toParkingLotPrice();
        insertParkingLotPrice.setCode(id);
        insertParkingLot.setParkingLotPrice(insertParkingLotPrice);

        parkingLotRepository.save(insertParkingLot);
    }

    @Transactional
    public void insertBatch(
            List<ParkingLot> updateParkingLotList, List<ParkingLotDetail> updateParkingLotDetailList,
            List<ParkingLotPrice> updateParkingLotPriceList
    ) {
        parkingLotRepository.batchInsert(updateParkingLotList);
        parkingLotDetailRepository.batchInsert(updateParkingLotDetailList);
        parkingLotPriceRepository.batchInsert(updateParkingLotPriceList);
    }

    @Transactional
    public void updateParkingLotBatch(List<ParkingLot> insertParkingLotList) {
        parkingLotRepository.batchUpdate(insertParkingLotList);
    }

    @Transactional
    public void updateParkingLotDetailBatch(List<ParkingLotDetail> insertParkingLotDetailList) {
        parkingLotDetailRepository.batchUpdate(insertParkingLotDetailList);
    }

    @Transactional
    public void updateParkLotPriceBatch(List<ParkingLotPrice> insertParkingLotPriceList) {
        parkingLotPriceRepository.batchUpdate(insertParkingLotPriceList);
    }

}

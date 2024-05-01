package com.parking.smart.sp_parking_scheduler.biz.parking.service;

import com.parking.smart.sp_parking_scheduler.biz.parking.model.ParkingInfo;
import com.parking.smart.sp_parking_scheduler.biz.parking.repository.ParkingLotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;

    public void updateParkingLot(ParkingInfo parkingInfo) {
        var parkingCode = Long.parseLong(parkingInfo.getParkingCode());
        var optional = parkingLotRepository.findById(parkingCode);

        if (optional.isEmpty()) {

        } else {
            var parkingLot = optional.get();
            var parkingLotDetail = parkingLot.getParkingLotDetail();
            var parkingLotPrice = parkingLot.getParkingLotPrice();
        }

    }

}

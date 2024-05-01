package com.parking.smart.sp_parking_scheduler.biz.parking.entity;

import com.parking.smart.sp_parking_scheduler.biz.common.CommonEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ParkingLot extends CommonEntity {

    private String name;

    private String address;

    private String tel;

    private LocalDateTime lastSync;

    private boolean realTimeInfoYN;

    private boolean isFree;

    private boolean isNightFree;

    private boolean isNightOpen;

    private boolean weekendFree;

    private boolean holidayFree;

    @OneToOne(mappedBy = "parkingLot")
    private ParkingLotPrice parkingLotPrice;

    @OneToOne(mappedBy = "parkingLot")
    private ParkingLotDetail parkingLotDetail;

}

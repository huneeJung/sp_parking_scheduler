package com.parking.smart.sp_parking_scheduler.biz.parking.entity;

import com.parking.smart.sp_parking_scheduler.biz.common.CommonEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ParkingLotDetail extends CommonEntity {

    private String typeCode;

    private String typeName;

    private Integer operateCode;

    private String operateName;

    private String weekdayOpen;

    private String weekdayClose;

    private String weekendOpen;

    private String weekendClose;

    private String holidayOpen;

    private String holidayClose;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @OneToOne
    @JoinColumn(name = "id")
    private ParkingLot parkingLot;

}

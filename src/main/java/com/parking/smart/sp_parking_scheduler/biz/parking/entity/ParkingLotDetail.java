package com.parking.smart.sp_parking_scheduler.biz.parking.entity;

import com.parking.smart.sp_parking_scheduler.biz.common.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PARKING_LOT_DETAIL")
public class ParkingLotDetail extends CommonEntity {

    @Column(name = "TYPE_CODE")
    private String typeCode;

    @Column(name = "TYPE_NAME")
    private String typeName;

    @Column(name = "OPERATION_CODE")
    private Integer operationCode;

    @Column(name = "OPERATION_NAME")
    private String operationName;

    @Column(name = "WEEKDAY_OPEN")
    private String weekdayOpen;

    @Column(name = "WEEKDAY_CLOSE")
    private String weekdayClose;

    @Column(name = "WEEKEND_OPEN")
    private String weekendOpen;

    @Column(name = "WEEKEND_CLOSE")
    private String weekendClose;

    @Column(name = "HOLIDAY_OPEN")
    private String holidayOpen;

    @Column(name = "HOLIDAY_CLOSE")
    private String holidayClose;

    @Column(name = "LATITUDE")
    private BigDecimal latitude;

    @Column(name = "LONGITUDE")
    private BigDecimal longitude;

    @OneToOne
    @JoinColumn(name = "CODE")
    private ParkingLot parkingLot;

}

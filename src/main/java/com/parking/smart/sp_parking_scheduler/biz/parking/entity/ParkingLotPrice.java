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
public class ParkingLotPrice extends CommonEntity {

    private Integer unitMinute;

    private BigDecimal unitPrice;

    private Integer extraMinute;

    private BigDecimal extraPrice;

    private BigDecimal dailyMaxAmt;

    private BigDecimal monthlyPass;

    @OneToOne
    @JoinColumn(name = "id")
    private ParkingLot parkingLot;

}

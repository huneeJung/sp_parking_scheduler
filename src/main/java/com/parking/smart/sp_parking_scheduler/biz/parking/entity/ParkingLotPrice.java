package com.parking.smart.sp_parking_scheduler.biz.parking.entity;

import com.parking.smart.sp_parking_scheduler.biz.common.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PARKING_LOT_PRICE")
public class ParkingLotPrice extends CommonEntity {

    @Column(name = "UNIT_MINUTE")
    private Integer unitMinute;

    @Column(name = "UNIT_PRICE")
    private BigDecimal unitPrice;

    @Column(name = "EXTRA_MINUTE")
    private Integer extraMinute;

    @Column(name = "EXTRA_PRICE")
    private BigDecimal extraPrice;

    @Column(name = "DAILY_MAX_PRICE")
    private BigDecimal dailyMaxPrice;

    @Column(name = "MONTHLY_PASS_PRICE")
    private BigDecimal monthlyPassPrice;

    @OneToOne
    @JoinColumn(name = "CODE")
    private ParkingLot parkingLot;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingLotPrice that = (ParkingLotPrice) o;
        return Objects.equals(unitMinute, that.unitMinute) && Objects.equals(unitPrice, that.unitPrice) && Objects.equals(extraMinute, that.extraMinute) && Objects.equals(extraPrice, that.extraPrice) && Objects.equals(dailyMaxPrice, that.dailyMaxPrice) && Objects.equals(monthlyPassPrice, that.monthlyPassPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unitMinute, unitPrice, extraMinute, extraPrice, dailyMaxPrice, monthlyPassPrice);
    }
}

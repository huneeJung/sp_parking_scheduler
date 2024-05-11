package com.parking.smart.sp_parking_scheduler.biz.parking.entity;

import com.parking.smart.sp_parking_scheduler.biz.common.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PARKING_LOT")
public class ParkingLot extends CommonEntity {

    @Column(name = "CODE", unique = true)
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ADDRESS")
    private String address;

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

    @Column(name = "IS_FREE")
    private Boolean isFree;

    @Column(name = "WEEKEND_FREE")
    private Boolean weekendFree;

    @Column(name = "HOLIDAY_FREE")
    private Boolean holidayFree;

    @OneToOne(mappedBy = "parkingLot", fetch = LAZY, cascade = ALL)
    private ParkingLotPrice parkingLotPrice;

    @OneToOne(mappedBy = "parkingLot", fetch = LAZY, cascade = ALL)
    private ParkingLotDetail parkingLotDetail;

    @Column(name = "LATITUDE", precision = 20, scale = 8)
    private BigDecimal latitude;

    @Column(name = "LONGITUDE", precision = 20, scale = 8)
    private BigDecimal longitude;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingLot that = (ParkingLot) o;
        return Objects.equals(code, that.code) && Objects.equals(name, that.name)
                && Objects.equals(address, that.address) && Objects.equals(weekdayOpen, that.weekdayOpen)
                && Objects.equals(weekdayClose, that.weekdayClose) && Objects.equals(weekendOpen, that.weekendOpen)
                && Objects.equals(weekendClose, that.weekendClose) && Objects.equals(holidayOpen, that.holidayOpen)
                && Objects.equals(holidayClose, that.holidayClose) && Objects.equals(isFree, that.isFree)
                && Objects.equals(weekendFree, that.weekendFree) && Objects.equals(holidayFree, that.holidayFree);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, address, weekdayOpen, weekdayClose, weekendOpen,
                weekendClose, holidayOpen, holidayClose, isFree, weekendFree, holidayFree);
    }
}



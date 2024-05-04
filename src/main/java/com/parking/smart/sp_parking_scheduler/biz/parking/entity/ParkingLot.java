package com.parking.smart.sp_parking_scheduler.biz.parking.entity;

import com.parking.smart.sp_parking_scheduler.biz.common.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
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

    @Column(name = "TEL")
    private String tel;

    @Column(name = "LAST_SYNC")
    private LocalDateTime lastSync;

    @Column(name = "REAL_TIME_INFO")
    private Integer realTimeInfo;

    @Column(name = "REAL_TIME_INFO_DESCRIPTION")
    private String realTimeInfoDescription;

    @Column(name = "NIGHT_OPEN")
    private String nightOpen;

    @Column(name = "IS_FREE")
    private Boolean isFree;

    @Column(name = "IS_NIGHT_FREE")
    private Boolean isNightFree;

    @Column(name = "WEEKEND_FREE")
    private Boolean weekendFree;

    @Column(name = "HOLIDAY_FREE")
    private Boolean holidayFree;

    @OneToOne(mappedBy = "parkingLot", fetch = LAZY, cascade = ALL)
    private ParkingLotPrice parkingLotPrice;

    @OneToOne(mappedBy = "parkingLot", fetch = LAZY, cascade = ALL)
    private ParkingLotDetail parkingLotDetail;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingLot that = (ParkingLot) o;
        return Objects.equals(name, that.name) && Objects.equals(address, that.address)
                && Objects.equals(tel, that.tel) && Objects.equals(lastSync, that.lastSync)
                && Objects.equals(realTimeInfoDescription, that.realTimeInfoDescription)
                && Objects.equals(realTimeInfo, that.realTimeInfo) && Objects.equals(nightOpen, that.nightOpen)
                && Objects.equals(isFree, that.isFree) && Objects.equals(isNightFree, that.isNightFree)
                && Objects.equals(weekendFree, that.weekendFree) && Objects.equals(holidayFree, that.holidayFree);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, tel, lastSync, realTimeInfo, realTimeInfoDescription, nightOpen, isFree, isNightFree, weekendFree, holidayFree);
    }
}



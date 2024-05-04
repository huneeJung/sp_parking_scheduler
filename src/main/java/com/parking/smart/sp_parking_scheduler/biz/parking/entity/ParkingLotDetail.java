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
@Table(name = "PARKING_LOT_DETAIL")
public class ParkingLotDetail extends CommonEntity {

    @Column(name = "CODE", unique = true)
    private String code;

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

    @Column(name = "LATITUDE", precision = 20, scale = 8)
    private BigDecimal latitude;

    @Column(name = "LONGITUDE", precision = 20, scale = 8)
    private BigDecimal longitude;

    @OneToOne
    @JoinColumn(name = "ID")
    private ParkingLot parkingLot;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingLotDetail that = (ParkingLotDetail) o;
        // 위도 경도는 같은 기존 데이터와 같은지 체크할때 필드 목록에서 제외
        // 같은 주차장인 중복 데이터가 존재하고 각각의 위도 경도 수치가 달라 매번 업데이트 수행하는 불필요한 오버헤드 발생
        return Objects.equals(code, that.code) && Objects.equals(typeCode, that.typeCode)
                && Objects.equals(typeName, that.typeName) && Objects.equals(operationCode, that.operationCode)
                && Objects.equals(operationName, that.operationName) && Objects.equals(weekdayOpen, that.weekdayOpen)
                && Objects.equals(weekdayClose, that.weekdayClose) && Objects.equals(weekendOpen, that.weekendOpen)
                && Objects.equals(weekendClose, that.weekendClose) && Objects.equals(holidayOpen, that.holidayOpen)
                && Objects.equals(holidayClose, that.holidayClose);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, typeCode, typeName, operationCode, operationName, weekdayOpen, weekdayClose, weekendOpen, weekendClose, holidayOpen, holidayClose, latitude, longitude);
    }
}

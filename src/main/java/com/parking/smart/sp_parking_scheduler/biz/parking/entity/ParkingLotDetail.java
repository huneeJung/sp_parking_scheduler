package com.parking.smart.sp_parking_scheduler.biz.parking.entity;

import com.parking.smart.sp_parking_scheduler.biz.common.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    @Column(name = "CAPACITY")
    private Integer capacity;

    @Column(name = "TYPE_CODE")
    private String typeCode;

    @Column(name = "TYPE_NAME")
    private String typeName;

    @Column(name = "OPERATION_CODE")
    private Integer operationCode;

    @Column(name = "OPERATION_NAME")
    private String operationName;

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

    @Column(name = "IS_NIGHT_FREE")
    private Boolean isNightFree;

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
        return Objects.equals(code, that.code) && Objects.equals(capacity, that.capacity) && Objects.equals(typeCode, that.typeCode)
                && Objects.equals(typeName, that.typeName) && Objects.equals(operationCode, that.operationCode)
                && Objects.equals(operationName, that.operationName) && Objects.equals(tel, that.tel)
                && Objects.equals(lastSync, that.lastSync) && Objects.equals(realTimeInfo, that.realTimeInfo)
                && Objects.equals(realTimeInfoDescription, that.realTimeInfoDescription)
                && Objects.equals(nightOpen, that.nightOpen) && Objects.equals(isNightFree, that.isNightFree);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, capacity, typeCode, typeName, operationCode, operationName,
                tel, lastSync, realTimeInfo, realTimeInfoDescription, nightOpen, isNightFree);
    }
}

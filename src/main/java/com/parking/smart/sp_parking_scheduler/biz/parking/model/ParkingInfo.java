package com.parking.smart.sp_parking_scheduler.biz.parking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLot;
import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLotDetail;
import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLotPrice;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.math.BigDecimal.ZERO;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParkingInfo {

    @JsonProperty("PARKING_CODE")
    private String code;

    @JsonProperty("PARKING_NAME")
    private String name;

    @JsonProperty("ADDR")
    private String address;

    @JsonProperty("TEL")
    private String tel;

    @JsonProperty("CAPACITY")
    private Integer capacity;

    @JsonProperty("PARKING_TYPE")
    private String typeCode;

    @JsonProperty("PARKING_TYPE_NM")
    private String typeName;

    @JsonProperty("OPERATION_RULE")
    private Integer operationCode;

    @JsonProperty("OPERATION_RULE_NM")
    private String operationName;

    @JsonProperty("QUE_STATUS")
    private Integer queStatus;

    @JsonProperty("QUE_STATUS_NM")
    private String queStatusNm;

    @JsonProperty("PAY_YN")
    private String free;

    @JsonProperty("PAY_NM")
    private String freeNm;

    @JsonProperty("NIGHT_FREE_OPEN")
    private String nightFree;

    @JsonProperty("NIGHT_FREE_OPEN_NM")
    private String nightFreeNm;

    @JsonProperty("WEEKDAY_BEGIN_TIME")
    private String weekdayOpen;

    @JsonProperty("WEEKDAY_END_TIME")
    private String weekdayClose;

    @JsonProperty("WEEKEND_BEGIN_TIME")
    private String weekendOpen;

    @JsonProperty("WEEKEND_END_TIME")
    private String weekendClose;

    @JsonProperty("HOLIDAY_BEGIN_TIME")
    private String holidayOpen;

    @JsonProperty("HOLIDAY_END_TIME")
    private String holidayClose;

    @JsonProperty("SYNC_TIME")
    private String lastSyncTime;

    @JsonProperty("SATURDAY_PAY_YN")
    private String weekendFree;

    @JsonProperty("SATURDAY_PAY_NM")
    private String weekendFreeNm;

    @JsonProperty("HOLIDAY_PAY_YN")
    private String holidayFree;

    @JsonProperty("HOLIDAY_PAY_NM")
    private String holidayFreeNm;

    @JsonProperty("FULLTIME_MONTHLY")
    private String monthlyPassPrice;

    @JsonProperty("RATES")
    private String basicParkingPrice;

    @JsonProperty("TIME_RATE")
    private Integer basicParkingTime;

    @JsonProperty("ADD_RATES")
    private String additionalParkingPrice;

    @JsonProperty("ADD_TIME_RATE")
    private Integer additionalParkingTime;

    @JsonProperty("DAY_MAXIMUM")
    private String dailyMaxAmt;

    @JsonProperty("LAT")
    private String lat;

    @JsonProperty("LNG")
    private String lng;

    public ParkingLot toParkingLot() {
        return ParkingLot.builder()
                .code(code)
                .name(name)
                .address(address)
                .weekdayOpen(weekdayOpen)
                .weekdayClose(weekdayClose)
                .weekendOpen(weekendOpen)
                .weekendClose(weekendClose)
                .holidayOpen(holidayOpen)
                .holidayClose(holidayClose)
                .isFree(!StringUtils.isBlank(free) && !this.free.equals("Y"))
                .weekendFree(!StringUtils.isBlank(weekendFree) && !weekendFree.equals("Y"))
                .holidayFree(!StringUtils.isBlank(holidayFree) && !holidayFree.equals("Y"))
                .latitude(StringUtils.isBlank(lat) ? ZERO : new BigDecimal(lat))
                .longitude(StringUtils.isBlank(lng) ? ZERO : new BigDecimal(lng))
                .build();

    }

    public ParkingLotDetail toParkingLotDetail() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime syncTime = StringUtils.isBlank(lastSyncTime) ? null : LocalDateTime.parse(lastSyncTime, formatter);
        return ParkingLotDetail.builder()
                .code(code)
                .capacity(capacity)
                .typeCode(typeCode)
                .typeName(typeName)
                .operationCode(operationCode)
                .operationName(operationName)
                .tel(tel)
                .lastSync(syncTime)
                .realTimeInfo(queStatus)
                .realTimeInfoDescription(queStatusNm)
                .nightOpen(nightFreeNm)
                .isNightFree(!StringUtils.isBlank(nightFree) && !this.nightFree.equals("Y"))
                .build();
    }

    public ParkingLotPrice toParkingLotPrice() {
        return ParkingLotPrice.builder()
                .code(code)
                .unitMinute(basicParkingTime)
                .unitPrice(StringUtils.isBlank(basicParkingPrice) ? ZERO : new BigDecimal(basicParkingPrice))
                .extraMinute(additionalParkingTime)
                .extraPrice(StringUtils.isBlank(additionalParkingPrice) ? ZERO : new BigDecimal(additionalParkingPrice))
                .dailyMaxPrice(StringUtils.isBlank(dailyMaxAmt) ? ZERO : new BigDecimal(dailyMaxAmt))
                .monthlyPassPrice(StringUtils.isBlank(monthlyPassPrice) ? ZERO : new BigDecimal(monthlyPassPrice))
                .build();
    }

}

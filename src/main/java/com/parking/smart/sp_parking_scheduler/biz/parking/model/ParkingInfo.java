package com.parking.smart.sp_parking_scheduler.biz.parking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingInfo {

    @JsonProperty(namespace = "PARKING_NAME")
    private String parkingName;

    @JsonProperty(namespace = "ADDR")
    private String addr;

    @JsonProperty(namespace = "PARKING_CODE")
    private String parkingCode;

    @JsonProperty(namespace = "PARKING_TYPE")
    private String parkingType;

    @JsonProperty(namespace = "PARKING_TYPE_NM")
    private String parkingTypeNm;

    @JsonProperty(namespace = "OPERATION_RULE")
    private String operationRule;

    @JsonProperty(namespace = "OPERATION_RULE_NM")
    private String operationRuleNm;

    @JsonProperty(namespace = "TEL")
    private String tel;

    @JsonProperty(namespace = "QUE_STATUS")
    private String queStatus;

    @JsonProperty(namespace = "QUE_STATUS_NM")
    private String queStatusNm;

    @JsonProperty(namespace = "CAPACITY")
    private Integer capacity;

    @JsonProperty(namespace = "PAY_YN")
    private Integer free;

    @JsonProperty(namespace = "PAY_NM")
    private String freeNm;

    @JsonProperty(namespace = "NIGHT_FREE_OPEN")
    private String nightFree;

    @JsonProperty(namespace = "NIGHT_FREE_OPEN_NM")
    private String nightFreeNm;

    @JsonProperty(namespace = "WEEKDAY_BEGIN_TIME")
    private String weekdayOpen;

    @JsonProperty(namespace = "WEEKDAY_END_TIME")
    private String weekdayClose;

    @JsonProperty(namespace = "WEEKEND_BEGIN_TIME")
    private String weekendOpen;

    @JsonProperty(namespace = "WEEKEND_END_TIME")
    private String weekendClose;

    @JsonProperty(namespace = "HOLIDAY_BEGIN_TIME")
    private String holidayOpen;

    @JsonProperty(namespace = "HOLIDAY_END_TIME")
    private String holidayClose;

    @JsonProperty(namespace = "SYNC_TIME")
    private String lastSyncTime;

    @JsonProperty(namespace = "SATURDAY_PAY_YN")
    private String weekendFree;

    @JsonProperty(namespace = "SATURDAY_PAY_NM")
    private String weekendFreeNm;

    @JsonProperty(namespace = "HOLIDAY_PAY_YN")
    private String holidayFree;

    @JsonProperty(namespace = "HOLIDAY_PAY_NM")
    private String holidayFreeNm;

    @JsonProperty(namespace = "FULLTIME_MONTHLY")
    private String monthlyPassPrice;

    @JsonProperty(namespace = "RATES")
    private String basicParkingPrice;

    @JsonProperty(namespace = "TIME_RATE")
    private String basicParkingTime;

    @JsonProperty(namespace = "ADD_RATES")
    private String additionalParkingPrice;

    @JsonProperty(namespace = "ADD_TIME_RATE")
    private String additionalParkingTime;

    @JsonProperty(namespace = "DAY_MAXIMUM")
    private String dailyMaxAmt;

    @JsonProperty(namespace = "LAT")
    private String lat;

    @JsonProperty(namespace = "LNG")
    private String lng;

}

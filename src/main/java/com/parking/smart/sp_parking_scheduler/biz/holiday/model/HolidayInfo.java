package com.parking.smart.sp_parking_scheduler.biz.holiday.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.parking.smart.sp_parking_scheduler.biz.holiday.entity.Holiday;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HolidayInfo {

    @JsonProperty("dateName")
    private String dateName;

    @JsonProperty("locdate")
    private String locdate;

    public Holiday toHoliday() {
        return Holiday.builder()
                .date(locdate)
                .dateName(dateName)
                .build();
    }
    
}

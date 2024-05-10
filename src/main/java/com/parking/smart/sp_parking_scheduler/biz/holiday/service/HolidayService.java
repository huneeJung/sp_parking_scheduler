package com.parking.smart.sp_parking_scheduler.biz.holiday.service;

import com.parking.smart.sp_parking_scheduler.biz.holiday.HolidayRepository;
import com.parking.smart.sp_parking_scheduler.biz.holiday.model.HolidayInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class HolidayService {

    private final HolidayRepository holidayRepository;

    @Transactional
    public int insertHoliday(HolidayInfo holidayInfo) {
        var optional = holidayRepository.findByDate(holidayInfo.getLocdate());
        if (optional.isEmpty()) {
            holidayRepository.save(holidayInfo.toHoliday());
            return 1;
        }
        return 0;
    }

}

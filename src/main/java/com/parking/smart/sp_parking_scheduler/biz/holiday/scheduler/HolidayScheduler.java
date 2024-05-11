package com.parking.smart.sp_parking_scheduler.biz.holiday.scheduler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parking.smart.sp_parking_scheduler.biz.holiday.model.HolidayInfo;
import com.parking.smart.sp_parking_scheduler.biz.holiday.service.HolidayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HolidayScheduler {

    private final HolidayService holidayService;
    @Value("${holiday.openapi.url}")
    private String url;
    @Value("${holiday.openapi.key}")
    private String authKey;

    // 매달 1일 새벽 3시에 실행
//    @Scheduled(cron = "0 0 3 1 * *")
    @Scheduled(fixedDelay = 50000000, initialDelay = 5000)
    public void syncHolidayInfo() {
        var start = Instant.now();
        log.info("Holiday Synchronize Start");

        var requestUrl = url + "ServiceKey=" + authKey
                + "&pageNo=1&numOfRows=365&solYear=" + LocalDateTime.now().getYear();

        // 인증키에 특수문자가 포함되어 있고, restTemplate 은 자동으로 특수문자를 utf-8로 인코딩하므로, 이 인코딩을 수행하지 않게 URI 로 웹핑
        // 문자열로 요청하게 되면, 특수문자가 포함된 authKey 값이 utf-8로 인코딩되어 변형이 됨
        var uri = URI.create(requestUrl);

        // API 호출 및 응답 처리
        var restTemplate = new RestTemplate();
        var response = restTemplate.exchange(uri, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Map<String, Object>>() {
        });

        var body = response.getBody();
        if (response.getStatusCode().value() != 200 || body == null || body.get("response") == null) {
            log.info("Holiday OpenAPI Request Failed ::: requestUrl {}", requestUrl);
        }

        var bodyResponse = (Map<String, Object>) body.get("response");
        body = (Map<String, Object>) bodyResponse.get("body");
        var totalCount = (int) body.get("totalCount");
        log.info("Holiday OpenAPI Request Size ::: totalCount {}", totalCount);
        if (totalCount == 0) {
            log.info("Holiday Synchronize End");
            return;
        }

        var items = (Map<String, Object>) body.get("items");
        var om = new ObjectMapper();
        var holidayInfoList = om.convertValue(items.get("item"), new TypeReference<List<HolidayInfo>>() {
        });

        var insertedCount = 0;
        for (HolidayInfo holidayInfo : holidayInfoList) {
            var isSuccess = holidayService.insertHoliday(holidayInfo);
            insertedCount += isSuccess;
        }

        var end = Instant.now();
        long workingTime = Duration.between(start, end).toMillis();
        log.info("Holiday Synchronize End ::: insertedCount {} ::: workTime {}ms", insertedCount, workingTime);
    }

}

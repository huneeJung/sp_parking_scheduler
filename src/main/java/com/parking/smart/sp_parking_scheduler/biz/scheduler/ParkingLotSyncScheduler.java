package com.parking.smart.sp_parking_scheduler.biz.scheduler;

import com.parking.smart.sp_parking_scheduler.biz.parking.model.ParkingInfo;
import com.parking.smart.sp_parking_scheduler.biz.parking.service.ParkingLotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParkingLotSyncScheduler {

    private final ParkingLotService parkingLotService;

    @Value("${parking.openapi.url}")
    private String url;
    @Value("${parking.openapi.key}")
    private String authKey;
    @Value("${parking.openapi.type}")
    private String dataType;
    @Value("${parking.openapi.service}")
    private String service;

    // 매일 새벽 3시에 실행
    @Scheduled(cron = "0 0 3 * * *")
    private void syncParkingLotInfo() {
        String pinUrl = url + File.separator + authKey + File.separator +
                dataType + File.separator + service;

        var startIndex = 1;
        var endIndex = 1000;
        var listSize = Integer.MAX_VALUE;
        while (startIndex <= listSize) {
            log.info("ParkingLot Synchronize Start ::: startIndex {} , endIndex {}", startIndex, endIndex);

            var requestUrl = pinUrl + File.separator + startIndex + File.separator + endIndex;
            RestTemplate restTemplate = new RestTemplateBuilder().build();
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    requestUrl, HttpMethod.GET, HttpEntity.EMPTY,
                    new ParameterizedTypeReference<>() {
                    });
            Map<String, Object> body = response.getBody();
            if (response.getStatusCode().value() != 200 || response.getBody() == null || body.get("GetParkInfo") == null) {
                log.info("ParkingLot OpenAPI Request Failed ::: requestUrl {}", requestUrl);
                break;
            }

            Map<String, Object> parkInfo = (Map<String, Object>) body.get("GetParkInfo");
            listSize = (int) parkInfo.get("list_total_count");
            List<ParkingInfo> row = (List<ParkingInfo>) parkInfo.get("row");

            for (ParkingInfo parkingInfo : row) {
                parkingLotService.updateParkingLot(parkingInfo);
            }

            startIndex += 1000;
            endIndex += 1000;
        }

        log.info("ParkingLot Synchronize End ::: listSize {}", listSize);
    }

}

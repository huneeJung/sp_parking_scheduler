package com.parking.smart.sp_parking_scheduler.biz.scheduler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLot;
import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLotDetail;
import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLotPrice;
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
import java.time.Duration;
import java.time.Instant;
import java.util.*;

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
//    @Scheduled(cron = "0 0 3 * * *")
    @Scheduled(fixedDelay = 50000000, initialDelay = 5000)
    private void syncParkingLotInfo() {

        Instant start = Instant.now();
        String pinUrl = url + File.separator + authKey + File.separator +
                dataType + File.separator + service;
        var startIndex = 1;
        var endIndex = 1000;
        var listSize = 0;

        do {
            log.info("ParkingLot Synchronize Start ::: startIndex {} , endIndex {}", startIndex, endIndex);
            var requestUrl = pinUrl + File.separator + startIndex + File.separator + endIndex;
            RestTemplate restTemplate = new RestTemplateBuilder().build();
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    requestUrl, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {
                    });
            Map<String, Object> body = response.getBody();
            if (response.getStatusCode().value() != 200 || body == null || body.get("GetParkInfo") == null) {
                log.info("ParkingLot OpenAPI Request Failed ::: requestUrl {}", requestUrl);
                break;
            }
            Map<String, Object> parkInfo = (Map<String, Object>) body.get("GetParkInfo");
            listSize = (int) parkInfo.get("list_total_count");
            ObjectMapper om = new ObjectMapper();
            List<ParkingInfo> parkingInfoList = om.convertValue(parkInfo.get("row"), new TypeReference<>() {
            });
            try {
                updateParkingLots(parkingInfoList);
            } catch (Exception e) {
                log.warn("ParkingLot OpenAPI Batch Failed ::: startIndex {} ::: endIndex {} ::: errorMessage {}",
                        startIndex, endIndex, e.getMessage(), e);
                // TODO : Slack 연동 및 알림 처리 코드 추가
            }
            startIndex += 1000;
            endIndex += 1000;
        } while (startIndex <= listSize);

        Instant end = Instant.now();
        long workingTime = Duration.between(start, end).toSeconds();

        log.info("ParkingLot Synchronize End ::: listSize {} ::: workTime {}", listSize, workingTime);
    }

    public void updateParkingLots(List<ParkingInfo> parkingInfoList) {

        List<ParkingLot> updateParkingLotList = new ArrayList<>();
        List<ParkingLotDetail> updateParkingLotDetailList = new ArrayList<>();
        List<ParkingLotPrice> updateParkingLotPriceList = new ArrayList<>();

        // 공영주차장 OpenAPI 동기화할 목록 1000건 Select
        Set<String> insertTargets = new HashSet<>(1000);
        for (ParkingInfo parkingInfo : parkingInfoList) {
            insertTargets.add(parkingInfo.getCode());
        }
        var selectedParkingLots = parkingLotService.findAnyByCodes(insertTargets);

        // Select 한 1000건의 ROW , PK로
        Map<String, ParkingLot> updateTargets = new HashMap<>();
        for (ParkingLot parkingLot : selectedParkingLots) {
            var id = parkingLot.getCode();
            updateTargets.put(id, parkingLot);
            insertTargets.remove(id);
        }

        List<ParkingLot> insertParkingLotList = null;
        List<ParkingLotDetail> insertParkingLotDetailList = null;
        List<ParkingLotPrice> insertParkingLotPriceList = null;

        boolean insertBatchMode = false;
        // insertTarget 100건 이상이면 Insert 배치 모드
        if (insertTargets.size() >= 100) {
            insertBatchMode = true;
            insertParkingLotList = new ArrayList<>();
            insertParkingLotDetailList = new ArrayList<>();
            insertParkingLotPriceList = new ArrayList<>();
        }

        for (ParkingInfo parkingInfo : parkingInfoList) {
            var id = parkingInfo.getCode();
            if (insertTargets.contains(id)) {
                // ParkingLot Insert
                if (insertBatchMode) {
                    // ParkingLot Insert 배치 목록 추가
                    var insertParkingLot = parkingInfo.toParkingLot();
                    insertParkingLot.setCode(id);
                    insertParkingLotList.add(insertParkingLot);
                    // ParkingLotDetail Insert 배치 목록 추가
                    var insertParkingLotDetail = parkingInfo.toParkingLotDetail();
                    insertParkingLotDetail.setParkingLot(insertParkingLot);
                    insertParkingLotDetailList.add(insertParkingLotDetail);
                    // ParkingLotPrice Insert 배치 목록 추가
                    var insertParkingLotPrice = parkingInfo.toParkingLotPrice();
                    insertParkingLotPrice.setParkingLot(insertParkingLot);
                    insertParkingLotPriceList.add(insertParkingLotPrice);
                } else {
                    parkingLotService.insertParkingLot(parkingInfo);
                }
            } else {
                // ParkingLot Update 배치 목록 추가
                var selectedParkingLot = updateTargets.get(id);
                var updateParkingLot = parkingInfo.toParkingLot();
                if (!selectedParkingLot.equals(updateParkingLot)) {
                    updateParkingLotList.add(updateParkingLot);
                }
                // ParkingLotDetail Update 배치 목록 추가
                var parkingLotDetail = selectedParkingLot.getParkingLotDetail();
                var updateParkingLotDetail = parkingInfo.toParkingLotDetail();
                if (!parkingLotDetail.equals(updateParkingLotDetail)) {
                    updateParkingLotDetailList.add(updateParkingLotDetail);
                }
                // ParkingLotPrice Update 배치 목록 추가
                var parkingLotPrice = selectedParkingLot.getParkingLotPrice();
                var updateParkingLotPrice = parkingInfo.toParkingLotPrice();
                if (!parkingLotPrice.equals(updateParkingLotPrice)) {
                    updateParkingLotPriceList.add(updateParkingLotPrice);
                }
            }
        }

        // 타겟 배치 처리
        parkingLotService.updateBatch(updateParkingLotList, updateParkingLotDetailList, updateParkingLotPriceList);
        if (insertBatchMode) {
            parkingLotService.insertBatch(insertParkingLotList, insertParkingLotDetailList, insertParkingLotPriceList);
        }
    }

}

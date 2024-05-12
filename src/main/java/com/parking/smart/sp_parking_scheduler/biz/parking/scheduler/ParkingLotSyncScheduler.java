package com.parking.smart.sp_parking_scheduler.biz.parking.scheduler;

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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
    @Value("${parking.insert.batch.flag:100}")
    private Integer insertBatchFlag;

    // 매일 새벽 3시에 실행
//    @Scheduled(cron = "0 0 3 * * *")
    @Scheduled(fixedDelay = 50000000, initialDelay = 5000)
    private void syncParkingLotInfo() {

        var start = Instant.now();
        var pinUrl = url + File.separator + authKey + File.separator +
                dataType + File.separator + service;
        var startIndex = 1;
        var endIndex = 1000;
        var totalCount = 0;

        do {
            log.info("ParkingLot Synchronize Start ::: startIndex {} , endIndex {}", startIndex, endIndex);
            var requestUrl = pinUrl + File.separator + startIndex + File.separator + endIndex;
            var restTemplate = new RestTemplateBuilder().build();
            var response = restTemplate.exchange(
                    requestUrl, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Map<String, Object>>() {
                    });
            var body = response.getBody();
            if (response.getStatusCode().value() != 200 || body == null || body.get(service) == null) {
                log.info("ParkingLot OpenAPI Request Failed ::: requestUrl {}", requestUrl);
                break;
            }
            var parkInfo = (Map<String, Object>) body.get(service);
            totalCount = (int) parkInfo.get("list_total_count");
            var om = new ObjectMapper();
            var parkingInfoList = om.convertValue(parkInfo.get("row"), new TypeReference<List<ParkingInfo>>() {
            });
            try {
                updateParkingLots(parkingInfoList);
            } catch (Exception e) {
                log.warn("ParkingLot OpenAPI Batch Failed ::: startIndex {} ::: endIndex {} ::: errorMessage {}",
                        startIndex, endIndex, e.getMessage(), e);
                // TODO : Slack 연동 및 알림 처리 코드 추가 CommonService 로 처리
            }
            startIndex += 1000;
            endIndex += 1000;
        } while (startIndex <= totalCount);

        var end = Instant.now();
        long workingTime = Duration.between(start, end).toMillis();
        log.info("ParkingLot Synchronize End ::: totalCount {} ::: workTime {}ms", totalCount, workingTime);
    }

    private void updateParkingLots(List<ParkingInfo> parkingInfoList) {

        var updateLotList = new ArrayList<ParkingLot>();
        var updateLotDetailList = new ArrayList<ParkingLotDetail>();
        var updateLotPriceList = new ArrayList<ParkingLotPrice>();

        // 공영주차장 OpenAPI 동기화할 목록 1000건 Select
        var insertTargets = new HashSet<String>(1000);
        for (ParkingInfo parkingInfo : parkingInfoList) {
            insertTargets.add(parkingInfo.getCode());
        }
        var selectedParkingLots = parkingLotService.findAnyByCodes(insertTargets);

        // UpdateTarget , InsertTarget 데이터 분기
        var updateTargets = new HashMap<String, ParkingLot>();
        for (ParkingLot parkingLot : selectedParkingLots) {
            var id = parkingLot.getCode();
            updateTargets.put(id, parkingLot);
            insertTargets.remove(id);
        }

        List<ParkingLot> insertLotList = null;
        List<ParkingLotDetail> insertLotDetailList = null;
        List<ParkingLotPrice> insertLotPriceList = null;

        var insertBatchMode = false;
        // insertTarget 설정값 이상 건수이면 Insert 배치 모드
        // Update 의 경우에는 항상 배치 모드
        // UpdateTarget 에서 기본/세부/가격 정보에 변경사항이 있는지 검증 후 업데이트하므로 사전에 몇건을 업데이트 할 것인지 확인 불가
        if (insertTargets.size() >= insertBatchFlag) {
            insertBatchMode = true;
            insertLotList = new ArrayList<>();
            insertLotDetailList = new ArrayList<>();
            insertLotPriceList = new ArrayList<>();
        }

        for (ParkingInfo parkingInfo : parkingInfoList) {
            var id = parkingInfo.getCode();
            // InsertTarget 인 경우
            if (insertTargets.contains(id)) {
                // ParkingLot Insert
                if (insertBatchMode) {
                    // ParkingLot Insert 배치 목록 추가
                    var insertParkingLot = parkingInfo.toParkingLot();
                    insertParkingLot.setCode(id);
                    insertLotList.add(insertParkingLot);
                    // ParkingLotDetail Insert 배치 목록 추가
                    var insertParkingLotDetail = parkingInfo.toParkingLotDetail();
                    insertParkingLotDetail.setCode(id);
                    insertLotDetailList.add(insertParkingLotDetail);
                    // ParkingLotPrice Insert 배치 목록 추가
                    var insertParkingLotPrice = parkingInfo.toParkingLotPrice();
                    insertParkingLotPrice.setCode(id);
                    insertLotPriceList.add(insertParkingLotPrice);
                } else {
                    log.info("New ParkingLot Insert ::: parkingCode {}", parkingInfo.getCode());
                    parkingLotService.insertParkingLot(parkingInfo);
                }
                // API 요청 데이터에 중복 데이터가 존재하므로 한번 추가된 데이터는 중복 추가 못하도록 삭제 처리
                insertTargets.remove(id);
            }
            // UpdateTarget 인 경우
            else if (updateTargets.get(id) != null) {
                var selectedParkingLot = updateTargets.get(id);
                // ParkingLot Update 배치 목록 추가
                var updateParkingLot = parkingInfo.toParkingLot();
                if (!selectedParkingLot.equals(updateParkingLot)) {
                    updateLotList.add(updateParkingLot);
                }
                // ParkingLotDetail Update 배치 목록 추가
                var parkingLotDetail = selectedParkingLot.getParkingLotDetail();
                var updateParkingLotDetail = parkingInfo.toParkingLotDetail();
                if (!parkingLotDetail.equals(updateParkingLotDetail)) {
                    updateLotDetailList.add(updateParkingLotDetail);
                }
                // ParkingLotPrice Update 배치 목록 추가
                var parkingLotPrice = selectedParkingLot.getParkingLotPrice();
                var updateParkingLotPrice = parkingInfo.toParkingLotPrice();
                if (!parkingLotPrice.equals(updateParkingLotPrice)) {
                    updateLotPriceList.add(updateParkingLotPrice);
                }
            }
        }

        // 변동된 정보가 존재하는 경우 테이블별로 배치 Update
        if (!updateLotList.isEmpty()) {
            parkingLotService.updateParkingLotBatch(updateLotList);
            log.info("ParkingLot Batch Updated ::: updateLotList size {}", updateLotList.size());
        }
        if (!updateLotDetailList.isEmpty()) {
            parkingLotService.updateParkingLotDetailBatch(updateLotDetailList);
            log.info("ParkingLotDetail Batch Updated ::: updateLotDetailList size {}", updateLotDetailList.size());
        }
        if (!updateLotPriceList.isEmpty()) {
            parkingLotService.updateParkLotPriceBatch(updateLotPriceList);
            log.info("ParkingLotPrice Batch Updated ::: updateLotPriceList size {}", updateLotPriceList.size());
        }
        // 신규 공영 주차장 정보 Insert
        if (insertBatchMode) {
            parkingLotService.insertBatch(insertLotList, insertLotDetailList, insertLotPriceList);
            log.info("ParkingLot Batch Inserted ::: insertLotList size {}", insertLotList.size());
            log.info("ParkingLotDetail Batch Inserted ::: insertLotDetailList size {}", insertLotDetailList.size());
            log.info("ParkingLotPrice Batch Inserted ::: insertLotPriceList size {}", insertLotPriceList.size());
        }
    }

}

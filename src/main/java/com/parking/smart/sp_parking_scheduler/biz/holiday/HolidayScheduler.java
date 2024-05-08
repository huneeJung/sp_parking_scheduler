package com.parking.smart.sp_parking_scheduler.biz.holiday;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
public class HolidayScheduler {

    @Value("${holiday.openapi.url}")
    private String url;
    @Value("${holiday.openapi.key}")
    private String authKey;

    // 매달 1일 새벽 3시에 실행
//    @Scheduled(cron = "0 0 3 1 * *")
    @Scheduled(fixedDelay = 50000000, initialDelay = 5000)
    public void syncHolidayInfo() {
        var requestUrl = url + "ServiceKey=" + authKey + "&pageNo=1&numOfRows=365";

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().addFirst(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.getMessageConverters().add(new Jaxb2RootElementHttpMessageConverter());

        // HTTP 요청 설정
        var url = "http://example.com/api/holidays";  // 실제 API 엔드포인트 URL로 대체해야 합니다.
        var headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
        var entity = new HttpEntity<>(headers);

// API 호출 및 응답 처리
//        ResponseEntity<HolidayResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, HolidayResponse.class);
//        HolidayResponse holidayResponse = response.getBody();


        ObjectMapper xmlMapper = new XmlMapper();
    }

}

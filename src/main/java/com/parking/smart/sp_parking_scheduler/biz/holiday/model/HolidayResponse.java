package com.parking.smart.sp_parking_scheduler.biz.holiday.model;

import java.util.List;

public class HolidayResponse {

    private Header header;

    private Body body;
}

class Header {

    private String resultCode;

    private String resultMsg;
}

class Body {

    private List<Item> items;

    private int numOfRows;

    private int pageNo;

    private int totalCount;

}

class Item {

    private String dateKind;

    private String dateName;

    private String isHoliday;

    private String locdate;

    private int seq;

}
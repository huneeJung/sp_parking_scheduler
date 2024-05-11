package com.parking.smart.sp_parking_scheduler.biz.parking.repository;

import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLot;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ParkingLotBatchRepository {

    // Mysql 은 시퀀스 채번 방식을 지원하지 않으며, Identity 채번 방식은 Batch Insert 지원하지 않음
    // 일반적으로 RDBMS 가 Mysql 인 경우 JPA 를 활용하여 배치를 수행하기 보다는 JdbcTemplate 으로 배치 처리 권장 속도적 우위
    private final JdbcTemplate jdbcTemplate;

    public void batchInsert(List<ParkingLot> batchList) {
        jdbcTemplate.batchUpdate(
                """
                            INSERT INTO PARKING_LOT (
                            name, address, weekday_open, weekday_close, weekend_open, weekend_close,
                            holiday_open, holiday_close, is_free, weekend_free, holiday_free,
                            latitude, longitude, created_date, created_name, modified_date, modified_name, code
                            ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),'SYSTEM',NOW(),'SYSTEM',?)
                            ON DUPLICATE KEY UPDATE code=code 
                        """,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        setStatement(ps, batchList.get(i));
                    }

                    @Override
                    public int getBatchSize() {
                        return batchList.size();
                    }
                }
        );
    }

    public void batchUpdate(List<ParkingLot> batchList) {
        jdbcTemplate.batchUpdate(
                """
                            UPDATE PARKING_LOT SET
                            name=?, address=?, weekday_open=?, weekday_close=?, weekend_open=?, weekend_close=?,
                            holiday_open=?, holiday_close=?, is_free=?, weekend_free=?, holiday_free=?, 
                            latitude=?, longitude=?, modified_date=NOW(), modified_name='SYSTEM'
                            where code=?
                        """,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        setStatement(ps, batchList.get(i));
                    }

                    @Override
                    public int getBatchSize() {
                        return batchList.size();
                    }
                }
        );
    }

    private void setStatement(PreparedStatement ps, ParkingLot parkingLot) throws SQLException {
        ps.setString(1, parkingLot.getName());
        ps.setString(2, parkingLot.getAddress());
        ps.setString(3, parkingLot.getWeekdayOpen());
        ps.setString(4, parkingLot.getWeekdayClose());
        ps.setString(5, parkingLot.getWeekendOpen());
        ps.setString(6, parkingLot.getWeekendClose());
        ps.setString(7, parkingLot.getHolidayOpen());
        ps.setString(8, parkingLot.getHolidayClose());
        ps.setBoolean(9, parkingLot.getIsFree());
        ps.setBoolean(10, parkingLot.getWeekendFree());
        ps.setBoolean(11, parkingLot.getHolidayFree());
        ps.setBigDecimal(12, parkingLot.getLatitude());
        ps.setBigDecimal(13, parkingLot.getLongitude());
        ps.setString(14, parkingLot.getCode());
    }
}

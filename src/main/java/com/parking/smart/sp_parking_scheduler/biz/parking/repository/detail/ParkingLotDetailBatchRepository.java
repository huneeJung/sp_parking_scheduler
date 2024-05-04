package com.parking.smart.sp_parking_scheduler.biz.parking.repository.detail;

import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLotDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ParkingLotDetailBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    public void batchInsert(List<ParkingLotDetail> batchList) {
        jdbcTemplate.batchUpdate(
                """
                            INSERT INTO PARKING_LOT_DETAIL (
                            type_code, type_name, operation_code, operation_name, weekday_open, weekday_close,
                            weekend_open, weekend_close, holiday_open, holiday_close, latitude, longitude,
                            created_date, created_name, modified_date, modified_name, code
                            ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,NOW(),'SYSTEM',NOW(),'SYSTEM',?)
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

    public void batchUpdate(List<ParkingLotDetail> batchList) {
        jdbcTemplate.batchUpdate(
                """
                            UPDATE PARKING_LOT_DETAIL SET
                            type_code=?, type_name=?, operation_code=?, operation_name=?, weekday_open=?, weekday_close=?,
                            weekend_open=?, weekend_close=?, holiday_open=?, holiday_close=?, latitude=?, longitude=?,
                            modified_date=NOW(), modified_name='SYSTEM'
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

    private void setStatement(PreparedStatement ps, ParkingLotDetail parkingLotDetail) throws SQLException {
        ps.setString(1, parkingLotDetail.getTypeCode());
        ps.setString(2, parkingLotDetail.getTypeName());
        ps.setInt(3, parkingLotDetail.getOperationCode());
        ps.setString(4, parkingLotDetail.getOperationName());
        ps.setString(5, parkingLotDetail.getWeekdayOpen());
        ps.setString(6, parkingLotDetail.getWeekdayClose());
        ps.setString(7, parkingLotDetail.getWeekendOpen());
        ps.setString(8, parkingLotDetail.getWeekendClose());
        ps.setString(9, parkingLotDetail.getHolidayOpen());
        ps.setString(10, parkingLotDetail.getHolidayClose());
        ps.setBigDecimal(11, parkingLotDetail.getLatitude());
        ps.setBigDecimal(12, parkingLotDetail.getLongitude());
        ps.setString(13, parkingLotDetail.getParkingLot().getCode());
    }
}

package com.parking.smart.sp_parking_scheduler.biz.parking.repository.lot;

import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLot;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ParkingLotBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    public void batchInsert(List<ParkingLot> batchList) {
        jdbcTemplate.batchUpdate(
                """
                            INSERT INTO PARKING_LOT (
                            name, address, tel, last_sync, real_time_info, real_time_info_description,
                            night_open, is_free, is_night_free, weekend_free, holiday_free,
                            created_date, created_name, modified_date, modified_name, code
                            ) VALUES (?,?,?,?,?,?,?,?,?,?,?,NOW(),'SYSTEM',NOW(),'SYSTEM',?)
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
                            name=?, address=?, tel=?, last_sync=?, real_time_info=?, real_time_info_description=?, 
                            night_open=?, is_free=?, is_night_free=?, weekend_free=?, holiday_free=?,
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

    private void setStatement(PreparedStatement ps, ParkingLot parkingLot) throws SQLException {
        ps.setString(1, parkingLot.getName());
        ps.setString(2, parkingLot.getAddress());
        ps.setString(3, parkingLot.getTel());
        ps.setTimestamp(4, Timestamp.valueOf(parkingLot.getLastSync()));
        ps.setInt(5, parkingLot.getRealTimeInfo());
        ps.setString(6, parkingLot.getRealTimeInfoDescription());
        ps.setString(7, parkingLot.getNightOpen());
        ps.setBoolean(8, parkingLot.getIsFree());
        ps.setBoolean(9, parkingLot.getIsNightFree());
        ps.setBoolean(10, parkingLot.getWeekendFree());
        ps.setBoolean(11, parkingLot.getHolidayFree());
        ps.setString(12, parkingLot.getCode());
    }
}

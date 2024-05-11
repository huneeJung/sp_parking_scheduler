package com.parking.smart.sp_parking_scheduler.biz.parking.repository.detail;

import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLotDetail;
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
public class ParkingLotDetailBatchRepository {

    // Mysql 은 시퀀스 채번 방식을 지원하지 않으며, Identity 채번 방식은 Batch Insert 지원하지 않음
    // 일반적으로 RDBMS 가 Mysql 인 경우 JPA 를 활용하여 배치를 수행하기 보다는 JdbcTemplate 으로 배치 처리 권장 속도적 우위
    private final JdbcTemplate jdbcTemplate;

    public void batchInsert(List<ParkingLotDetail> batchList) {
        jdbcTemplate.batchUpdate(
                """
                            INSERT INTO PARKING_LOT_DETAIL (
                            capacity, type_code, type_name, operation_code, operation_name,
                            tel, last_sync, real_time_info, real_time_info_description,
                            night_open, is_night_free,
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

    public void batchUpdate(List<ParkingLotDetail> batchList) {
        jdbcTemplate.batchUpdate(
                """
                            UPDATE PARKING_LOT_DETAIL SET
                            capacity=?, type_code=?, type_name=?, operation_code=?, operation_name=?,
                            tel=?, last_sync=?, real_time_info=?, real_time_info_description=?,
                            night_open=?, is_night_free=?, modified_date=NOW(), modified_name='SYSTEM'
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
        ps.setInt(1, parkingLotDetail.getCapacity());
        ps.setString(2, parkingLotDetail.getTypeCode());
        ps.setString(3, parkingLotDetail.getTypeName());
        ps.setInt(4, parkingLotDetail.getOperationCode());
        ps.setString(5, parkingLotDetail.getOperationName());
        ps.setString(6, parkingLotDetail.getTel());
        ps.setTimestamp(7, Timestamp.valueOf(parkingLotDetail.getLastSync()));
        ps.setInt(8, parkingLotDetail.getRealTimeInfo());
        ps.setString(9, parkingLotDetail.getRealTimeInfoDescription());
        ps.setString(10, parkingLotDetail.getNightOpen());
        ps.setBoolean(11, parkingLotDetail.getIsNightFree());
        ps.setString(12, parkingLotDetail.getCode());
    }
}

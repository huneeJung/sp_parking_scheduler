package com.parking.smart.sp_parking_scheduler.biz.parking.repository.price;

import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLotPrice;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class ParkingLotPriceBatchRepositoryImpl implements ParkingLotPriceBatchRepository {

    // Mysql 은 시퀀스 채번 방식을 지원하지 않으며, Identity 채번 방식은 Batch Insert 지원하지 않음
    // 일반적으로 RDBMS 가 Mysql 인 경우 JPA 를 활용하여 배치를 수행하기 보다는 JdbcTemplate 으로 배치 처리 권장 속도적 우위
    private final JdbcTemplate jdbcTemplate;

    public void batchInsert(List<ParkingLotPrice> batchList) {
        jdbcTemplate.batchUpdate(
                """
                            INSERT INTO PARKING_LOT_PRICE (
                            unit_minute, unit_price, extra_minute, extra_price,
                            daily_max_price, monthly_pass_price,
                            created_date, created_name, modified_date, modified_name, code
                            ) VALUES (?,?,?,?,?,?,NOW(),'SYSTEM',NOW(),'SYSTEM',?)
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

    public void batchUpdate(List<ParkingLotPrice> batchList) {
        jdbcTemplate.batchUpdate(
                """
                            UPDATE PARKING_LOT_PRICE SET
                            unit_minute=?, unit_price=?, extra_minute=?, extra_price=?,
                            daily_max_price=?, monthly_pass_price=?,
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

    private void setStatement(PreparedStatement ps, ParkingLotPrice parkingLotPrice) throws SQLException {
        ps.setInt(1, parkingLotPrice.getUnitMinute());
        ps.setBigDecimal(2, parkingLotPrice.getUnitPrice());
        ps.setInt(3, parkingLotPrice.getExtraMinute());
        ps.setBigDecimal(4, parkingLotPrice.getExtraPrice());
        ps.setBigDecimal(5, parkingLotPrice.getDailyMaxPrice());
        ps.setBigDecimal(6, parkingLotPrice.getMonthlyPassPrice());
        ps.setString(7, parkingLotPrice.getCode());
    }
}

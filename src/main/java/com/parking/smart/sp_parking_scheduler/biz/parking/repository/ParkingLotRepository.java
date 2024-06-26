package com.parking.smart.sp_parking_scheduler.biz.parking.repository;

import com.parking.smart.sp_parking_scheduler.biz.parking.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long>, ParkingLotBatchRepository {

    @Query("""
            SELECT pl FROM ParkingLot pl
            JOIN FETCH pl.parkingLotDetail pld
            JOIN FETCH pl.parkingLotPrice plp
            WHERE pl.code IN :codeList""")
    List<ParkingLot> findByCodeIn(@Param("codeList") Set<String> codeList);

}


package com.parking.smart.sp_parking_scheduler.biz.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class CommonEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @CreatedDate
    @Column(name = "CREATED_DATE", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdDate;

    @Column(name = "CREATED_NAME", updatable = false, columnDefinition = "VARCHAR(20) DEFAULT 'SYSTEM'")
    private String createdName;

    @LastModifiedDate
    @Column(name = "MODIFIED_DATE", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime modifiedDate;

    @Column(name = "MODIFIED_NAME", columnDefinition = "VARCHAR(20) DEFAULT 'SYSTEM'")
    private String modifiedName;

}
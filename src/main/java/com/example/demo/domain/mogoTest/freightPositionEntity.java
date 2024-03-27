package com.example.demo.domain.mogoTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "plbcFreightPosition") // 실제 몽고 DB 컬렉션 이름
@Data
@NoArgsConstructor
@AllArgsConstructor
public class freightPositionEntity {

    private String vehicleId;
    private String vehiclePositionLatitude;
    private String vehiclePositionLongitude;
    private String vehiclePositionSpeed;
    private String vehiclePositionDateTime;
    private LocalDateTime registDatetime;

}

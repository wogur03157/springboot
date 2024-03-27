package com.example.demo.domain.mogoTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "plbcVehicleRoute") // 실제 몽고 DB 컬렉션 이름
@Data
@NoArgsConstructor
@AllArgsConstructor
public class vehicleRouteEntity {

    private String vehicleId;
    private String vehiclePositionLatitude;
    private String vehiclePositionLongitude;
    private int vehiclePositionSpeed;
    private String vehiclePositionDateTime;
    private String registDatetime;

}

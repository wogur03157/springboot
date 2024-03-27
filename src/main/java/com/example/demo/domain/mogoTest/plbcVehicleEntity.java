package com.example.demo.domain.mogoTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "plbcVehicle") // 실제 몽고 DB 컬렉션 이름
@Data
@NoArgsConstructor
@AllArgsConstructor
public class plbcVehicleEntity {

    private String vehicleId;
    private String vehicleType;
    private String vehicleEmploy;
    private String registId;
}

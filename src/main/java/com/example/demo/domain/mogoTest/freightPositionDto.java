package com.example.demo.domain.mogoTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class freightPositionDto {

    public String vehicleId;
    public String vehiclePositionLatitude;
    public String vehiclePositionLongitude;
    public String vehiclePositionSpeed;
    public String vehiclePositionDateTime;
    public LocalDateTime registDatetime;
}

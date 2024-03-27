package com.example.demo.domain.mogoTest;

import com.example.demo.utils.excel.ExcelColumn;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "plbcManifest") // 실제 몽고 DB 컬렉션 이름
@Data
@NoArgsConstructor
@AllArgsConstructor
@Nullable
public class plbcManifestEntity {
    @Id
    @ExcelColumn(headerName = "id")
    private String id;

    @ExcelColumn(headerName = "coarriUnbSender")
    private String coarriUnbSender;

    @ExcelColumn(headerName = "coarriDlKubun")
    private String coarriDlKubun;

    @ExcelColumn(headerName = "coarriOperatorCode")
    private String coarriOperatorCode;

    @ExcelColumn(headerName = "coarriCallsign")
    private String coarriCallsign;

    @ExcelColumn(headerName = "coarriVesselName")
    private String coarriVesselName;

    @ExcelColumn(headerName = "coarriArrivalYear")
    private String coarriArrivalYear;

    @ExcelColumn(headerName = "coarriVesselCode")
    private String coarriVesselCode;

    @ExcelColumn(headerName = "coarriVoyage")
    private String coarriVoyage;

    @ExcelColumn(headerName = "coarriContainer")
    private String coarriContainer;

    @ExcelColumn(headerName = "coarriFullEmpty")
    private String coarriFullEmpty;

    @ExcelColumn(headerName = "coarriStatus")
    private String coarriStatus;

    @ExcelColumn(headerName = "coarriDateTime")
    private String coarriDateTime;

    @ExcelColumn(headerName = "coarriPort")
    private String coarriPort;

    @ExcelColumn(headerName = "coarriWeight")
    private String coarriWeight;

    @ExcelColumn(headerName = "coarriBlNumber")
    private String coarriBlNumber;

    @ExcelColumn(headerName = "coarriBooking")
    private String coarriBooking;

    @ExcelColumn(headerName = "coarriMrn")
    private String coarriMrn;

    @ExcelColumn(headerName = "coarriSeal")
    private String coarriSeal;

    @ExcelColumn(headerName = "coarriTemperature")
    private String coarriTemperature;

    @ExcelColumn(headerName = "coarriCelFah")
    private String coarriCelFah;

    @ExcelColumn(headerName = "coarriImdg")
    private String coarriImdg;

    @ExcelColumn(headerName = "coarriUndg")
    private String coarriUndg;

    @ExcelColumn(headerName = "coarriFilename")
    private String coarriFilename;

    @ExcelColumn(headerName = "coarriCreation")
    private LocalDateTime coarriCreation;

    @ExcelColumn(headerName = "ctioSeq")
    private String ctioSeq;

    @ExcelColumn(headerName = "remark")
    private String remark;

    @ExcelColumn(headerName = "rgstId")
    private String rgstId;

    @ExcelColumn(headerName = "rgstDem")
    private LocalDateTime rgstDem;

    @ExcelColumn(headerName = "chgId")
    private String chgId;

    @ExcelColumn(headerName = "chgDtm")
    private LocalDateTime chgDtm;
}

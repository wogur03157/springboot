package com.example.demo.domain.mogoTest;

import com.example.demo.utils.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "plbcContainerIoResult") // 실제 몽고 DB 컬렉션 이름
@Data
@NoArgsConstructor
@AllArgsConstructor
public class plbcContainerIoResultEntity {

    @ExcelColumn(headerName = "_id")
    private String _id;

    @ExcelColumn(headerName = "copionSeq")
    private String copionSeq;

    @ExcelColumn(headerName = "ctiorReusltSts")
    private String ctiorReusltSts;

    @ExcelColumn(headerName = "ctiorResultDhms")
    private String ctiorResultDhms;

    @ExcelColumn(headerName = "ctiorReusltSts2")
    private String ctiorReusltSts2;

    @ExcelColumn(headerName = "ctiorReusltSts3")
    private String ctiorReusltSts3;

    @ExcelColumn(headerName = "ctiorReusltSts4")
    private String ctiorReusltSts4;

    @ExcelColumn(headerName = "ctiorReusltSts5")
    private String ctiorReusltSts5;

    @ExcelColumn(headerName = "remark")
    private String remark;

    @ExcelColumn(headerName = "rgstId")
    private String rgstId;

    @ExcelColumn(headerName = "rgstDem")
    private String rgstDem;

    @ExcelColumn(headerName = "chgId")
    private String chgId;

    @ExcelColumn(headerName = "chgDtm")
    private String chgDtm;

}

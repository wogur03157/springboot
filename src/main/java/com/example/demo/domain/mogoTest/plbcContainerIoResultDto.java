package com.example.demo.domain.mogoTest;

import com.example.demo.utils.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "plbcContainerIoResult") // 실제 몽고 DB 컬렉션 이름
public class plbcContainerIoResultDto {

    private String _id;

    private String copionSeq;

    private String ctiorReusltSts;

    private String ctiorResultDhms;

    private String ctiorReusltSts2;

    private String ctiorReusltSts3;

    private String ctiorReusltSts4;

    private String ctiorReusltSts5;

    private String remark;

    private String rgstId;

    private String rgstDem;

    private String chgId;

    private String chgDtm;

    private List<String> data;

}

package com.example.demo.domain.mogoTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinDto {

    private String ctioSeq;
    private String vehicleId;
    private String carrierId;
    private String ctioVehicleRefNo;
    private String ctioCarrierRefNm;
    private String ctioIoFlag;
    private String ctioRcvDtm;
    private String ctioResponseFlag;
    private String ctioBkgDhms;
    private String remark;
    private String ctioFaultDesc;
    private String tmnlId;
    private String ctioTmnlRefNm;
    private String ctioCntrNo1;
    private String ctioCntrNo2;
    private String ctioCntrNo1FmFlag;
    private String ctioCntrNo2FmFlag;
    private String ctioCntr11Iso;
    private String ctioCntr12Iso;
    private String ctioReserved1;
    private String ctioReserved2;
    private String ctioReserved3;
    private String rgstId;
    private String rgstDem;
    private String chgId;
    private LocalDateTime chgDtm;
    private String ctiorSeq;
    private String copionSeq;
    private String ctiorReusltSts;
    private LocalDateTime ctiorResultDhms;
    private String ctiorReusltSts2;
    private String ctiorReusltSts3;
    private String ctiorReusltSts4;
    private String ctiorReusltSts5;
    private String rgstId2;
    private String rgstDem2;
    private String chgId2;
    private LocalDateTime chgDtm2;

}

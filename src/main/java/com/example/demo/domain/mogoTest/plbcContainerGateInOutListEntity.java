package com.example.demo.domain.mogoTest;

import com.example.demo.utils.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "plbcContainerGateInOutList") // 실제 몽고 DB 컬렉션 이름
@Data
@NoArgsConstructor
@AllArgsConstructor
public class plbcContainerGateInOutListEntity {
    @ExcelColumn(headerName = "terminalCode")
    private String terminalCode;

    @ExcelColumn(headerName = "inOut")
    private String inOut;

    @ExcelColumn(headerName = "containerNo")
    private String containerNo;

    @ExcelColumn(headerName = "terminalShipVoyageNo")
    private String terminalShipVoyageNo;

    @ExcelColumn(headerName = "terminalShipYear")
    private int terminalShipYear;

    @ExcelColumn(headerName = "shippingCode")
    private String shippingCode;

    @ExcelColumn(headerName = "containerSize")
    private int containerSize;

    @ExcelColumn(headerName = "containerType")
    private int containerType;

    @ExcelColumn(headerName = "fullEmptyType")
    private String fullEmptyType;

    @ExcelColumn(headerName = "containerWeight")
    private double containerWeight;

//    private String bookingNo;
//    private String truckerId;
//    private String truckerNo;
//    private String operationStatus;
//    private String containerCategory;
//    private String dangerousCode;
//    private String dangerousYesNo;
//    private String locationInformation;
//    private String inOutGateDate;
//    private String registId;
//    private LocalDateTime registDateTime;
}

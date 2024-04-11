package com.example.demo.domain.mogoTest;

import com.example.demo.utils.csv.CsvRender;
import com.example.demo.utils.excel.ExcelRender;
import com.example.demo.utils.json.JsonRender;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class mongoController {

    private final ExcelRender excelRender;
    private final CsvRender csvRender;
    private final JsonRender jsonRender;
    @GetMapping(value = "/excelCall")
    public void excelCall(HttpServletResponse res,@RequestBody(required = false) Map<String, String> requestBody) throws Exception {
//        String entityClassName = requestBody.get("entityClassName");
//        Class<?> entityClass = Class.forName(entityClassName);

        long startTime = System.currentTimeMillis();

//        excelRender.setExcelRender(entityClass);
//        excelRender.renderExcel(entityClass);

//        excelRender.setExcelRender(plbcManifestEntity.class);
//        excelRender.renderExcel(plbcManifestEntity.class);

        excelRender.setExcelRender(plbcContainerIoResultEntity.class);
        excelRender.renderExcel(plbcContainerIoResultEntity.class);

        excelRender.writeWorkbook(res);
        long endTime = System.currentTimeMillis();
        long totalTimeMillis = (endTime - startTime);
        System.out.println("Total excel time taken: " + totalTimeMillis + " 초");

    }
    @GetMapping(value = "/csvCall")
    public void csvCall(HttpServletResponse res,@RequestBody(required = false) Map<String, String> requestBody) throws Exception {
//        String entityClassName = requestBody.get("entityClassName");
//        Class<?> entityClass = Class.forName(entityClassName);
        long startTime = System.currentTimeMillis();
        csvRender.exportCsv(res,JoinDto.class);
//         csvRender.exportCsv(res,entityClass);
//        csvRender.exportCsv(res,plbcContainerIoResultEntity.class);
//        csvRender.exportCsv(res,plbcContainerIoResultDto.class);
//        csvRender.exportCsv(res,plbcManifestEntity.class);
        long endTime = System.currentTimeMillis();
        long totalTimeMillis = endTime - startTime;
        System.out.println("Total csv time taken: " + totalTimeMillis + " milliseconds");
    }

    @GetMapping(value = "/jsonCall")
    public void jsonCall(HttpServletResponse res,@RequestBody(required = false) Map<String, String> requestBody) throws Exception {
//        String entityClassName = requestBody.get("entityClassName");
        long startTime = System.currentTimeMillis();

//        jsonRender.exportToJSON(res,"plbcContainerIoResult");
//        jsonRender.JoinToJSON(res,"plbcContainerIoResult");
        jsonRender.exportJoinedDataToJSON(res);
        long endTime = System.currentTimeMillis();
        long totalTimeMillis = endTime - startTime;
        System.out.println("Total json time taken: " + totalTimeMillis + " milliseconds");
    }

}

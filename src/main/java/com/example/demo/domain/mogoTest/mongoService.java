package com.example.demo.domain.mogoTest;

import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class mongoService {
    private final MongoTemplate mongoTemplate;

    public void excel() throws Exception {
        long startTime = System.currentTimeMillis();
        try (Workbook workbook = new SXSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");

            AtomicInteger rowNum = new AtomicInteger(0);

            // Add column headers
            Row headerRow = sheet.createRow(rowNum.getAndIncrement());
            Class<?> entityClass = plbcContainerGateInOutListEntity.class;
            Field[] fields = entityClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                headerRow.createCell(i).setCellValue(field.getName());
            }


            // Query MongoDB for data and write to Excel directly
            mongoTemplate.stream(new Query(), plbcContainerGateInOutListEntity.class).limit(1000000).forEach(entity -> {
                Row row = sheet.createRow(rowNum.getAndIncrement());
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    field.setAccessible(true);
                    try {
                        Object value = field.get(entity);
                        if (value != null) {
                            row.createCell(i).setCellValue(value.toString());
                        } else {
                            row.createCell(i).setCellValue("");
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });

            FileOutputStream fileOut = new FileOutputStream("data.xlsx");
            workbook.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        long totalTimeMillis = (endTime - startTime);
        System.out.println("Total time taken: " + totalTimeMillis + " 초");
    }



}



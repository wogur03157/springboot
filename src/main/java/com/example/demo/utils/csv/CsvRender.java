package com.example.demo.utils.csv;

import com.example.demo.domain.mogoTest.JoinDto;
import com.example.demo.domain.mogoTest.plbcContainerIoResultDto;
import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.spi.CloseableIterator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
@RequiredArgsConstructor
public class CsvRender {

//    private final MongoTemplate mongoTemplate;
    private final JdbcTemplate jdbcTemplate;
//    public void exportCsv(HttpServletResponse res, Class<?> entityClass) {
//        int pageSize = 1000000; // 페이지 크기를 조절할 수 있음
//        try {
//            // Set HTTP response headers
//            res.setContentType("text/csv; charset=UTF-8");
//            res.setHeader("Content-Disposition", "attachment; filename=\"export.csv\"");
//
//            try (OutputStream os = res.getOutputStream();
//                 ZipOutputStream zos = new ZipOutputStream(os);
//                 CSVWriter writer = new CSVWriter(new OutputStreamWriter(zos, StandardCharsets.UTF_8))) {
//                int fileCount = 1;
//                int page = 0;
//                while (true) {
//                    String fileName = "export_" + fileCount + ".csv";
//                    ZipEntry entry = new ZipEntry(fileName);
//                    zos.putNextEntry(entry);
//
//                    // Write headers
//                    writeHeaders(writer, entityClass);
//
//                    // Write data
//                    if (!writeData(writer, entityClass, pageSize, page)) {
//                        break; // No more data
//                    }
//
//                    zos.closeEntry();
//                    fileCount++;
//                    page++;
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to export CSV", e);
//        }
//    }
//
//    private boolean writeData(CSVWriter writer, Class<?> entityClass, int pageSize, int page) {
//        int pageNum = 0; // 현재 페이지의 데이터 수
//        int lim=100000;
//        while (true) {
//            // 페이징 처리
//            int skip = page * lim;
//
//            Aggregation aggregation = Aggregation.newAggregation(
//                    // $match: copionSeq가 null이 아닌 문서만 선택
//                    Aggregation.match(Criteria.where("copionSeq").ne(null)),
//
//                    // $lookup: plbcContainerInOut 컬렉션과 조인
//                    Aggregation.lookup("plbcContainerInOut", "copionSeq", "_id", "data"),
//
//                    // 페이징 처리
//                    Aggregation.skip(skip),
//                    Aggregation.limit(lim) // 남은 페이지 크기만큼 데이터 가져오기
//            );
//
//            // Aggregation을 실행하고 결과를 페이징 처리하여 가져옴
//            AggregationResults<?> results = mongoTemplate.aggregate(aggregation, "plbcContainerIoResult", plbcContainerIoResultDto.class);
//            List<?> entities = results.getMappedResults();
//
//            if (entities.isEmpty()) {
//                return false; // No more data
//            }
//
//            Field[] fields = plbcContainerIoResultDto.class.getDeclaredFields();
//            List<String[]> csvLines = entities.stream()
//                    .map(entity -> getCsvLine(entity, fields))
//                    .collect(Collectors.toList());
//            writer.writeAll(csvLines);
//
//            pageNum += entities.size();
//
//            if (pageNum >= pageSize) {
//                return true; // 데이터가 페이지 크기만큼 가져왔으므로 다음 페이지로 넘어감
//            }
//        }
//    }
    public void exportCsv(HttpServletResponse res, Class<?> entityClass) {
        int pageSize = 50000; // 페이지 크기를 조절할 수 있음
        try {
            // Set HTTP response headers
            res.setContentType("text/csv; charset=UTF-8");
            res.setHeader("Content-Disposition", "attachment; filename=\"export.csv\"");

            try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(res.getOutputStream(), StandardCharsets.UTF_8))) {
                // Write headers
                writeHeaders(writer, entityClass);

                // Write data
                writeData(writer, entityClass, pageSize);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to export CSV", e);
        }
    }

    private void writeHeaders(CSVWriter writer, Class<?> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        String[] headers = Arrays.stream(fields)
                .map(Field::getName)
                .toArray(String[]::new);
        writer.writeNext(headers);
    }

    private void writeData(CSVWriter writer, Class<?> entityClass, int pageSize)  {
        int page = 0;
//        while (true) {
//            Query query = new Query().limit(pageSize).skip(page * pageSize);
//            List<?> entities = mongoTemplate.find(query, entityClass);
//            Aggregation aggregation = Aggregation.newAggregation(
//                    // $match: copionSeq가 null이 아닌 문서만 선택
//                    Aggregation.match(Criteria.where("copionSeq").ne(null)),
//
//                    // $lookup: plbcContainerInOut 컬렉션과 조인
//                    Aggregation.lookup("plbcContainerInOut", "copionSeq", "_id", "data"),
//
//                    // 페이징 처리
//                    Aggregation.skip(page * pageSize),
//                    Aggregation.limit(pageSize)
//            );
//            // Aggregation을 실행하고 결과를 페이징 처리하여 가져옴
//            AggregationResults<?> results = mongoTemplate.aggregate(aggregation, "plbcContainerIoResult", plbcContainerIoResultDto.class);
            String sqlQuery = "SELECT i.*, r.* FROM container_in_out i JOIN container_io_result r ON i.ctio_seq = r.copion_seq";
        writer.writeAll(jdbcTemplate.query(sqlQuery, rs -> {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<String[]> lines = new ArrayList<>();
            while (rs.next()) {
                String[] line = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    line[i] = rs.getString(i + 1);
                }
                lines.add(line);
            }
            return lines;
        }));

//            List<?> entities = results.getMappedResults();
//            if (entities.isEmpty()) return; // No more data

//            Field[] fields=JoinDto.class.getDeclaredFields();
//            List<String[]> csvLines = entities.stream()
//                    .map(entity -> getCsvLine(entity, fields))
//                    .collect(Collectors.toList());
//            writer.writeAll(entities);
//            entities.stream()
//                .map(entity -> getCsvLine(entity, entity.getClass().getDeclaredFields()))
//                    .forEach(csvLine -> {
//                            writer.writeNext(csvLine);
//                    });
            //             Write lines to CSV
//            for (Object entity : entities) {
//                String[] csvLine = getCsvLine(entity, entityClass.getDeclaredFields());
//                writer.writeNext(csvLine);
//            }
//            page++;
//            System.out.println(page);
//            if (page >= 20) return; // Limit export to 100 pages
//        }
    }

    private String[] getCsvLine(Object entity, Field[] fields) {
        return Arrays.stream(fields)
                .map(field -> {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(entity);
                        return (value != null) ? value.toString() : "";
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to export CSV", e);
                    }
                })
                .toArray(String[]::new);
    }

}

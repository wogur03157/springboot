package com.example.demo.utils.json;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;

import org.bson.Document;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class JsonRender {
    private final MongoTemplate mongoTemplate;
//    public void exportToJSON(HttpServletResponse response, String collectionName) {
//        response.setContentType("application/json; charset=UTF-8");
//        response.setHeader("Content-Disposition", "attachment; filename=\"export.json\"");
//
//        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()))){
//            Iterator<Document> iterator = mongoTemplate.getCollection(collectionName).aggregate(
//                    Arrays.asList(
//                            Aggregates.match(
//                                    Filters.ne("copionSeq", "")
//                            ),
//                            Aggregates.lookup("plbcContainerInOut", "copionSeq", "_id", "data"),
//                            Aggregates.limit(1000000)
//                    )
//            ).allowDiskUse(true).iterator();
//
//            while (iterator.hasNext()) {
//                Document document = iterator.next();
//                String json = document.toJson();
//                writer.write(json + "\n");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
public void JoinToJSON(HttpServletResponse response, String collectionName) {
    response.setContentType("application/json; charset=UTF-8");
    response.setHeader("Content-Disposition", "attachment; filename=\"export.json\"");

    try {
        // 스트리밍을 위한 버퍼 사용
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
        ExecutorService executorService = Executors.newFixedThreadPool(10); // 적절한 스레드 풀 크기 선택

        // MongoDB에서 데이터를 스트리밍하는 비동기 작업 시작
        CompletableFuture<Void> exportFuture = CompletableFuture.runAsync(() -> {
            try {
                mongoTemplate.getCollection(collectionName).aggregate(
                                Arrays.asList(
                                        Aggregates.match(Filters.ne("copionSeq", "")),
                                        Aggregates.lookup("plbcContainerInOut", "copionSeq", "_id", "data"),
                                        Aggregates.limit(1000000)
                                )
                        ).allowDiskUse(true)
                        .batchSize(5000) // 작은 배치 크기 선택
                        .forEach(document -> {
                            try {
                                String json = document.toJson();
                                writer.write(json + "\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
            } finally {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, executorService);

        // 비동기 작업 완료 대기
        exportFuture.get();
    } catch (IOException | InterruptedException | ExecutionException e) {
        e.printStackTrace();
    }
}


    public void exportToJSON(HttpServletResponse response, String collectionName) {
        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"export.json\"");

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()))) {
            // Start JSON array
            writer.write("[");

            // Stream data directly from the specified collection
            mongoTemplate.getCollection(collectionName).find().limit(1000000).batchSize(5000).forEach((Consumer<Document>) document -> {
                try {
                    String json = document.toJson();
                    // Write document to response stream
                    writer.write(json + ",\n"); // Add comma for all but the last document
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // End JSON array
            writer.write("]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;
    public void exportJoinedDataToJSON(HttpServletResponse response) throws IOException{
        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"export.json\"");
//        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE); // Set content type to octet-stream
//        response.setHeader("Content-Disposition", "attachment; filename=\"export.json\""); // Set filename for download

        try (OutputStream outputStream = response.getOutputStream()) {

            // Create SQL query to select joined data from multiple tables
            String sqlQuery;
//            if(sql){
                sqlQuery="SELECT * FROM container_io_result";
//            } else {
//                sqlQuery = "SELECT i.*, r.* FROM container_in_out i JOIN container_io_result r ON i.ctio_seq = r.copion_seq";
//            }

            entityManager.unwrap(Session.class).doWork(connection -> {
                try (PreparedStatement statement = connection.prepareStatement(sqlQuery);
                     ResultSet resultSet = statement.executeQuery()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    int batchSize = 10000; // Adjust batch size as needed

                    boolean firstRow = true;
                    while (resultSet.next()) {
                        if (!firstRow) {
                            outputStream.write(",".getBytes()); // Add comma for all but the first row
                        } else {
                            firstRow = false;
                        }

                        // Serialize a batch of rows to JSON format and write to output stream
                        List<Map<String, Object>> rows = new ArrayList<>();
                        for (int i = 0; i < batchSize && resultSet.next(); i++) {
                            Map<String, Object> rowMap = new HashMap<>();
                            for (int j = 1; j <= columnCount; j++) {
                                String columnName = metaData.getColumnLabel(j);
                                Object columnValue = resultSet.getObject(j);
                                rowMap.put(columnName, columnValue != null ? columnValue : ""); // Use ternary operator to handle null values
                            }
                            rows.add(rowMap);
                        }
                        objectMapper.writeValue(outputStream, rows);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}




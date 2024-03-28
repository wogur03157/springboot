package com.example.demo.utils.csv;

import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CsvRender {

    private final MongoTemplate mongoTemplate;
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

    private void writeData(CSVWriter writer, Class<?> entityClass, int pageSize) {
        int page = 0;
        while (true) {
            Query query = new Query().limit(pageSize).skip(page * pageSize);
            List<?> entities = mongoTemplate.find(query, entityClass);

            if (entities.isEmpty()) break; // No more data

            // Write lines to CSV
            for (Object entity : entities) {
                String[] csvLine = getCsvLine(entity, entityClass.getDeclaredFields());
                writer.writeNext(csvLine);
            }
            page++;
            System.out.println(page);
            if (page >= 20) break; // Limit export to 100 pages
        }
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
//    public void exportCsv(HttpServletResponse res, Class<?> entityClass) {
//
//        int pageSize = 50000; // 페이지 크기를 조절할 수 있음
//        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(res.getOutputStream(), StandardCharsets.UTF_8))) {
//            // Set HTTP response headers
//            res.setContentType("text/csv; charset=UTF-8");
//            res.setHeader("Content-Disposition", "attachment; filename=\"export.csv\"");
//            // Get fields
//            Field[] fields = entityClass.getDeclaredFields();
//            String[] headers = Arrays.stream(fields)
//                    .map(Field::getName)
//                    .toArray(String[]::new);
//            writer.writeNext(headers); // Write headers
//            // Paginate data and write to CSV
//            int page = 0;
//            while (true) {
//                Query query = new Query().limit(pageSize).skip(page * pageSize);
//                List<?> entities = mongoTemplate.find(query, entityClass);
//
//                if (entities.isEmpty()) break; // No more data
//
//                // Write lines to CSV
//                for (Object entity : entities) {
//                    String[] csvLine = Arrays.stream(fields)
//                            .map(field -> {
//                                field.setAccessible(true);
//                                try {
//                                    Object value = field.get(entity);
//                                    return (value != null) ? value.toString() : "";
//                                } catch (IllegalAccessException e) {
//                                    throw new RuntimeException("Failed to export CSV", e);
//                                }
//                            })
//                            .toArray(String[]::new);
//                    writer.writeNext(csvLine);
//                }
//                page++;
//                System.out.println(page);
//                if (page >= 20) break; // Limit export to 100 pages
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to export CSV", e);
//        }
//    }
//    public void exportCsv(HttpServletResponse res, Class<?> entityClass) {
//
//        int pageSize = 50000; // 페이지 크기를 조절할 수 있음
//        try {
//            // Create a temporary file
//            File tempFile = File.createTempFile("export", ".csv");
//
//            try (CSVWriter writer = new CSVWriter(new FileWriter(tempFile))) {
//                // Get fields
//                Field[] fields = entityClass.getDeclaredFields();
//                String[] headers = Arrays.stream(fields)
//                        .map(Field::getName)
//                        .toArray(String[]::new);
//                writer.writeNext(headers); // Write headers
//                // Paginate data and write to CSV
//                int page = 0;
//                while (true) {
//                    Query query = new Query().limit(pageSize).skip(page * pageSize);
//                    List<?> entities = mongoTemplate.find(query, entityClass);
//
//                    if (entities.isEmpty()) break; // No more data
//
//                    // Write lines to CSV
//                    for (Object entity : entities) {
//                        String[] csvLine = Arrays.stream(fields)
//                                .map(field -> {
//                                    field.setAccessible(true);
//                                    try {
//                                        Object value = field.get(entity);
//                                        return (value != null) ? value.toString() : "";
//                                    } catch (IllegalAccessException e) {
//                                        throw new RuntimeException("Failed to export CSV", e);
//                                    }
//                                })
//                                .toArray(String[]::new);
//                        writer.writeNext(csvLine);
//                    }
//                    page++;
//                    System.out.println(page);
//                    if (page >= 20) break; // Limit export to 100 pages
//                }
//            }
//
//            // Set HTTP response headers
//            res.setContentType("text/csv; charset=UTF-8");
//            res.setHeader("Content-Disposition", "attachment; filename=\"export.csv\"");
//
//            // Copy the content of the temporary file to the response output stream
//            try (InputStream inputStream = new FileInputStream(tempFile)) {
//                FileCopyUtils.copy(inputStream, res.getOutputStream());
//                res.flushBuffer();
//            }
//
//            // Delete the temporary file
//            tempFile.delete();
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to export CSV", e);
//        }
//    }
}

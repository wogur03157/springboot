package com.example.demo.utils.json;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

@Component
public class DataExporter {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public void exportDataToJson(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setHeader("Content-Disposition", "attachment; filename=output.json");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("[");
            final boolean[] firstRow = {true};

            jdbcTemplate.query("SELECT * FROM container_io_result limit 100000", rs -> {
                if (!firstRow[0]) {
                    writer.println(",");
                } else {
                    firstRow[0] = false;
                }
                try {
                    writer.print(resultSetToJson(rs));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            writer.println("]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String resultSetToJson(ResultSet rs) throws SQLException {
        StringBuilder json = new StringBuilder("{");
        ResultSetMetaData metaData = rs.getMetaData();
        int numColumns = metaData.getColumnCount();
        for (int i = 1; i <= numColumns; i++) {
            json.append("\"").append(metaData.getColumnLabel(i)).append("\": ");
            json.append("\"").append(rs.getString(i)).append("\"");
            if (i < numColumns) {
                json.append(",");
            }
        }
        json.append("}");
        return json.toString();
    }
//    public void exportDataToJson(HttpServletResponse response) {
//        response.setContentType("application/json");
//        response.setHeader("Content-Disposition", "attachment; filename=output.json");
//
//        try (PrintWriter writer = new PrintWriter(
//                new OutputStreamWriter(new FileOutputStream("output.json"), "UTF-8"))) {
//            writer.println("[");
//
//            jdbcTemplate.query("SELECT * FROM container_io_result", (RowCallbackHandler) rs -> {
//                try {
//                    writer.print(resultSetToJson(rs));
//                    writer.println(",");
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            });
//
//            writer.println("]");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String resultSetToJson(ResultSet rs) throws SQLException {
//        StringBuilder json = new StringBuilder("{");
//        int numColumns = rs.getMetaData().getColumnCount();
//        for (int i = 1; i <= numColumns; i++) {
//            json.append("\"").append(rs.getMetaData().getColumnLabel(i)).append("\": ");
//            json.append("\"").append(rs.getString(i)).append("\"");
//            if (i < numColumns) {
//                json.append(",");
//            }
//        }
//        json.append("}");
//        return json.toString();
//    }
}

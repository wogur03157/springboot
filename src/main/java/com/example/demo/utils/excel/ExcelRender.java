package com.example.demo.utils.excel;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class ExcelRender {
    private SXSSFWorkbook wb;
    private Row row;
    private Cell cell;
    private Class<?> clazz;
    private Sheet sheet;
    @Setter
    private int maxRowCanBeRendered = 500000;
    private int ROW_START_INDEX = 0;
    private List<Integer> nums;

    private final MongoTemplate mongoTemplate;
    public void setExcelRender(Class<?> clazz) {
        wb = new SXSSFWorkbook();
        wb.setCompressTempFiles(true); // 압축여부
        this.clazz = clazz; // @ExcelColumn이 가지고 있는 class
        nums = new ArrayList<>();
    }
    private Stream<?> fetchDataAsStream(Class<?> entity, int pageSize, int pageNumber) {
        // 데이터를 몽고디비에서 가져오는 로직 추가
        Query query = new Query().limit(pageSize).skip(pageSize * (pageNumber - 1));
        return mongoTemplate.stream(query, entity);
    }

    /*
     * excel 데이터 그리기
     */
    public void renderExcel(Class<?> entity) throws IOException {
        for (int pageNumber = 1; pageNumber <= 20; pageNumber++) { // 페이지 번호가 100을 초과하면 종료
            Stream<?> dataStream = fetchDataAsStream(entity, 50000, pageNumber);

            // 데이터가 없으면 헤더만 그리고 반환
            List<?> dataList = dataStream.collect(Collectors.toList());
            if (dataList.isEmpty()) {
                createNewSheetWithHeader();
                return;
            }

            // 시트와 헤더 생성
            createNewSheetWithHeader();
            AtomicInteger rowIndex = new AtomicInteger(ROW_START_INDEX + 1);
            for (Object renderedData : dataList) {
                // body에 cell 위치
                renderContent(renderedData, rowIndex.getAndIncrement());
            }

            // sheet 캐쉬 비우기
            ((SXSSFSheet) sheet).flushRows(dataList.size());
            // list clear
            dataList.clear();
        }
    }
    /*
      SXSSFWorkbook 쓰기(엑셀 다운로드)
     */
    public void writeWorkbook(HttpServletResponse res) throws IOException {
        res.setContentType("application/vnd.ms-excel");
        res.setHeader("Content-Disposition", "attachment; filename=\"export.xlsx\"");
        res.setCharacterEncoding(StandardCharsets.UTF_8.name());
        OutputStream target = res.getOutputStream();
        wb.write(target);
        // 임시파일 삭제
        wb.dispose();
    }

    /* sheet 생성 및 header 만들어주는 메소드 호출 */
    public void createNewSheetWithHeader() {
        sheet = wb.createSheet();
        renderHeadersWithNewSheet(sheet, ROW_START_INDEX);
    }

    /* 헤더 생성 */
    protected void renderHeadersWithNewSheet(Sheet sheet, int rowIndex) {
        Row row = sheet.createRow(rowIndex);
        AtomicInteger counter = new AtomicInteger(0);
        this.nums.clear();
        Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(ExcelColumn.class))
                .forEachOrdered(f -> {
                    ExcelColumn ec = f.getAnnotation(ExcelColumn.class);
                    cell = row.createCell(counter.getAndIncrement());
                    cell.setCellValue(ec.headerName());
                    this.nums.add(ec.headerName().length());
                });
    }

    protected void renderContent(Object data, int rowIndex) {
        row = sheet.createRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        AtomicInteger counter = new AtomicInteger(0);
        Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ExcelColumn.class))
                .forEach(field -> {
                    int index = counter.getAndIncrement();
                    cell = row.createCell(index);
                    if (cell == null) {
                        cell = row.createCell(index);
                    }
                    Object value = this.getValue(data, field);
                    this.maxColumnWidth(index, value);
                });
        this.updateColumnWidths();
    }
    private String getMethodName(Field field) {
        String methodName = field.getName();
        return "get" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
    }
    // obj value
    private Object getValue(Object data, Field field) {
        try {
            if (data == null) {
                return "";
            }
            String methodName = getMethodName(field);
            MethodHandle methodHandle = MethodHandles.lookup()
                    .findVirtual(data.getClass(),methodName, MethodType.methodType(field.getType()));
            this.applyCellStyle(cell, methodHandle.invoke(data), methodName);
            return methodHandle.invoke(data);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }

    // cellStyle 적용
    private void applyCellStyle(Cell cell, Object value, String column) {
        if (value instanceof Long && !column.toUpperCase().contains("UID")) {
            cell.setCellValue(value == null ? 0 : Long.parseLong(value.toString()));
        } else {
            cell.setCellValue(value == null ? "" : value.toString());
        }
    }
    // 오토사이징
    private void updateColumnWidths() {
        IntStream.range(0, nums.size())
                .forEach(i -> sheet.setColumnWidth(i, (nums.get(i) * 256) + 1048));
    }

    // length 최대값 구하기
    private void maxColumnWidth(int column, Object value) {
        int length = value == null ? 0 : value.toString().length();
        nums.set(column, Math.max(nums.get(column), length));
    }
}



// src/main/java/com/example/demo/controller/ReportController.java
package com.example.demo.controller;

import com.example.demo.model.SensorData;
import com.example.demo.service.SensorService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final SensorService sensorService;

    @GetMapping("/dashboard/export")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportSensorsToXlsx() throws IOException {
        List<SensorData> sensors = sensorService.getAllSensors();

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sensors");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Bus ID");
        headerRow.createCell(2).setCellValue("Sensor Type");
        headerRow.createCell(3).setCellValue("Value");
        headerRow.createCell(4).setCellValue("Timestamp");

        int rowNum = 1;
        for (SensorData sensor : sensors) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(sensor.getId());
            row.createCell(1).setCellValue(sensor.getBus().getId());
            row.createCell(2).setCellValue(sensor.getSensorType().toString());
            row.createCell(3).setCellValue(sensor.getValue());
            row.createCell(4).setCellValue(sensor.getTimestamp().toString());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        byte[] data = out.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "sensors.xlsx");
        headers.setContentLength(data.length);

        return ResponseEntity.ok().headers(headers).body(data);
    }
}
package com.example.shopgiayonepoly.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExcelExportService {

    public void exportListToExcel(List<Object[]> dataList) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String formattedDate = sdf.format(new Date());

// Đảm bảo thư mục tồn tại
        String directoryPath = "D:/danhSachGiaoDich/";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Tạo thư mục nếu chưa tồn tại
        }

        String filePath = directoryPath + formattedDate + "_transaction_export.xlsx";

        // Tạo một workbook mới
        Workbook workbook = new XSSFWorkbook();

        // Tạo một sheet mới
        Sheet sheet = workbook.createSheet("Sheet 1");

        // Tạo tiêu đề bảng
        String[] headers = {"STT", "Mã GD", "Số hóa đơn", "Mã GD Bank", "Nội dung", "Ngân hàng", "Số tiền", "Trạng thái", "Ngày tạo"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Ghi dữ liệu vào sheet
        int rowNum = 1;
        double totalAmount = 0;
        for (Object[] dataRow : dataList) {
            Row row = sheet.createRow(rowNum++);
            int cellNum = 0;

            // Thêm cột STT (Số thứ tự)
            Cell sttCell = row.createCell(cellNum++);
            sttCell.setCellValue(rowNum - 1); // Số thứ tự bắt đầu từ 1

            // Các cột còn lại
            for (Object field : dataRow) {
                Cell cell = row.createCell(cellNum++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                } else if (field instanceof Double) {
                    cell.setCellValue((Double) field);
                    if (cellNum == 7) { // Cột "Số tiền" nằm ở chỉ số 7 (index 7)
                        totalAmount += (Double) field;
                    }
                } else if (field instanceof Boolean) {
                    cell.setCellValue((Boolean) field);
                } else if (field != null) {
                    cell.setCellValue(field.toString());
                }
            }
        }

// Thêm dòng tổng tiền cuối cùng
        Row totalRow = sheet.createRow(rowNum);
        Cell totalLabelCell = totalRow.createCell(5); // Cột "Tổng tiền" nằm ở chỉ số 8
        totalLabelCell.setCellValue("Tổng tiền:");

// Xử lý chuỗi "Số tiền" để cộng dồn chính xác
        for (Object[] dataRow : dataList) {
            String amountString = (String) dataRow[5]; // Giả sử cột "Số tiền" là ở chỉ số 5
            // Loại bỏ ký tự không phải số (VD: " VNÐ")
            amountString = amountString.replaceAll("[^\\d]", "");
            try {
                double amount = Double.parseDouble(amountString);
                totalAmount += amount;
            } catch (NumberFormatException e) {
                // Nếu có lỗi khi chuyển đổi, in lỗi và bỏ qua giá trị đó
                System.err.println("Lỗi khi chuyển đổi số tiền: " + amountString);
            }
        }

        // Định dạng số với dấu phân cách hàng nghìn và thêm đơn vị "VNĐ"
        DecimalFormat df = new DecimalFormat("#,###.##");
        String formattedAmount = df.format(totalAmount) + " VNĐ";

// Đặt giá trị vào ô "Tổng tiền"
        Cell totalValueCell = totalRow.createCell(6); // Cột tiếp theo để hiện tổng số tiền
        totalValueCell.setCellValue(formattedAmount);

// Ghi workbook vào file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            System.out.println("File Excel đã được lưu tại: " + filePath);
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi file: " + e.getMessage());
            throw e;
        } finally {
            workbook.close();
        }
    }
}

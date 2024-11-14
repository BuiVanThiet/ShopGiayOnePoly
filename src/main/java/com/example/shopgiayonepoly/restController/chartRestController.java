package com.example.shopgiayonepoly.restController;

import com.example.shopgiayonepoly.dto.request.ProductInfoDto;
import com.example.shopgiayonepoly.dto.request.Statistics;
import com.example.shopgiayonepoly.dto.request.StatusBill;
import com.example.shopgiayonepoly.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class chartRestController {
    @Autowired
    ChartService chartService;
    @GetMapping("/MonthlyStatistics")
    public List<Statistics> getStatistics() {
        return chartService.findMonthlyStatistics();
    }

    @GetMapping("/TodayStatistics")
    public List<Statistics> getTodayStatisticsList(){
        return chartService.findTodayStatistics();
    }

    @GetMapping("/Last7DaysStatistics")
    public List<Statistics> getLast7DaysStatistics(){
        return chartService.findLast7DaysStatistics();
    }

    @GetMapping("/AnnualStatistics")
    public List<Statistics> getAnnualStatistics(){
        return chartService.getAnnualStatistics();
    }

    @GetMapping("/productSales")
    public ResponseEntity<Map<String, Object>> getProductSales(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {

        Page<ProductInfoDto> productSales = chartService.getProductSalesPage(page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("content", productSales.getContent());
        response.put("pageable", productSales.getPageable());
        response.put("totalPages", productSales.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getProductsByDateRange")
    public ResponseEntity<Map<String, Object>> getProductSalesByDateRange(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        // Không cần chuyển đổi từ String thành Date ở đây nữa
        // Trực tiếp sử dụng startDate và endDate dưới dạng String

        // Lấy dữ liệu từ service
        Page<ProductInfoDto> productSales = chartService.getProductSalesPageByDateRange(page, size, startDate, endDate);

        // Chuẩn bị dữ liệu trả về
        Map<String, Object> response = new HashMap<>();
        response.put("content", productSales.getContent());
        response.put("pageable", productSales.getPageable());
        response.put("totalPages", productSales.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/statusBillsMonth")
    public ResponseEntity<List<StatusBill>> getBillsWithStatusDescription() {
        List<StatusBill> statusBills = chartService.findBillsWithStatusDescription();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .body(statusBills);
    }

    @GetMapping("/statusBillsToday")
    public ResponseEntity<List<StatusBill>> getStatusCountForToday() {
        List<StatusBill> statusBills = chartService.getStatusCountForToday();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .body(statusBills);
    }

    @GetMapping("/statusBillsLast7Days")
    public ResponseEntity<List<StatusBill>> findStatusCountsForLast7Days() {
        List<StatusBill> statusBills = chartService.findStatusCountsForLast7Days();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .body(statusBills);
    }

    @GetMapping("/statusBillsYear")
    public ResponseEntity<List<StatusBill>> countStatusByYear() {
        List<StatusBill> statusBills = chartService.countStatusByYear();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .body(statusBills);
    }

    @PostMapping("/getStatisticsByDateRange")
    public ResponseEntity<?> getStatisticsByDateRange(@RequestBody Map<String, String> dateRange) {
        String startDate = dateRange.get("startDate");
        String endDate = dateRange.get("endDate");
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        // Kiểm tra xem ngày kết thúc cách ngày bắt đầu không quá 10 ngày
        long daysBetween = ChronoUnit.DAYS.between(start, end);
        if (daysBetween > 9) {
            return ResponseEntity.badRequest().body("Khoảng cách giữa ngày bắt đầu và ngày kết thúc không được vượt quá 10 ngày.");
        }

        // Nếu hợp lệ, gọi service để lấy dữ liệu
        return ResponseEntity.ok(chartService.findStatisticsByDateRange(startDate, endDate));
    }
    @GetMapping("/statisticsByRange")
    public ResponseEntity<List<StatusBill>> getBillStatistics(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {

        List<StatusBill> statusBills = chartService.getBillStatisticsByDateRange(startDate, endDate);
        return ResponseEntity.ok(statusBills);
    }
}

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/statusBills")
    public ResponseEntity<List<StatusBill>> getBillsWithStatusDescription() {
        List<StatusBill> statusBills = chartService.findBillsWithStatusDescription();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .body(statusBills);
    }


    @GetMapping("/productSales")
    public ResponseEntity<Page<ProductInfoDto>> getProductSales(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {

        Page<ProductInfoDto> productSales = chartService.getProductSalesPage(page, size);
        return new ResponseEntity<>(productSales, HttpStatus.OK);
    }

}

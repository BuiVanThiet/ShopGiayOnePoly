package com.example.shopgiayonepoly.restController;

import com.example.shopgiayonepoly.dto.request.ProductInfoDto;
import com.example.shopgiayonepoly.dto.request.Statistics;
import com.example.shopgiayonepoly.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

//    @GetMapping("/top-products")
//    public List<ProductInfoDto> getTopProducts() {
//        return chartService.getProductSales();
//    }
}

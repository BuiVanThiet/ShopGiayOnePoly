package com.example.shopgiayonepoly.restController;

import com.example.shopgiayonepoly.dto.request.MonthlyStatistics;
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
    @GetMapping("/statistics")
    public List<MonthlyStatistics> getStatistics() {
        return chartService.findMonthlyStatistics(); // Trả về dữ liệu thống kê
    }
}

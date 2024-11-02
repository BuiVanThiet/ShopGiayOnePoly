package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.dto.request.MonthlyStatistics;

import java.util.Date;
import java.util.List;

public interface ChartService {
    long monthlyBill();
    long totalMonthlyBill();
    long totalMonthlyInvoiceProducts();
    long billOfTheDay();
    long totalPriceToday();
    List<Date> findLastBillDates();
    List<MonthlyStatistics> findMonthlyStatistics();
}

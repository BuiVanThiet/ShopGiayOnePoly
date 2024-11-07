package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.dto.request.ProductInfoDto;
import com.example.shopgiayonepoly.dto.request.Statistics;

import java.util.Date;
import java.util.List;

public interface ChartService {
    long monthlyBill();

    long totalMonthlyBill();

    long totalMonthlyInvoiceProducts();

    long billOfTheDay();

    long totalPriceToday();

    List<Date> findLastBillDates();

    List<Statistics> findMonthlyStatistics();

    List<Statistics> findTodayStatistics();

    List<Statistics> findLast7DaysStatistics();

    List<Statistics> getAnnualStatistics();
    List<ProductInfoDto> getProductSales();
}

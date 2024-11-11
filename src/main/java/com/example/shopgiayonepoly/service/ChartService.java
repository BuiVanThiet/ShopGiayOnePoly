package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.dto.request.ProductInfoDto;
import com.example.shopgiayonepoly.dto.request.Statistics;
import com.example.shopgiayonepoly.dto.request.StatusBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    Page<ProductInfoDto> getProductSalesPage(int page, int size);

    List<StatusBill> findBillsWithStatusDescription();

    List<StatusBill> getStatusCountForToday();

    List<StatusBill> findStatusCountsForLast7Days();

    List<StatusBill> countStatusByYear();
}

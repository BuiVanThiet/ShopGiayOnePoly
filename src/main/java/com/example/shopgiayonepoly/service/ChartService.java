package com.example.shopgiayonepoly.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ChartService {
    long monthlyBill();
    long totalMonthlyBill();
    long totalMonthlyInvoiceProducts();
    long billOfTheDay();
    long totalPriceToday();
    List<Date> findLastBillDates();
}

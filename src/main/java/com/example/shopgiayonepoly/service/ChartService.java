package com.example.shopgiayonepoly.service;

public interface ChartService {
    long monthlyBill();
    long totalMonthlyBill();
    long totalMonthlyInvoiceProducts();
    long billOfTheDay();
    long totalPriceToday();
}

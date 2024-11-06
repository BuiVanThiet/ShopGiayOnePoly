package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.dto.request.Statistics;
import com.example.shopgiayonepoly.repositores.ChartRepository;
import com.example.shopgiayonepoly.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ChartImplement implements ChartService {
    @Autowired
    ChartRepository chartRepository;

    @Override
    public long monthlyBill() {
        return chartRepository.monthlyBill();
    }

    @Override
    public long totalMonthlyBill() {
        return chartRepository.totalMonthlyBill();
    }

    @Override
    public long totalMonthlyInvoiceProducts() {
        return chartRepository.totalMonthlyInvoiceProducts();
    }

    @Override
    public long billOfTheDay() {
        return chartRepository.billOfTheDay();
    }

    @Override
    public long totalPriceToday() {
        return chartRepository.totalPriceToday();
    }

    @Override
    public List<Date> findLastBillDates() {
        return chartRepository.findLastBillDates();
    }

    @Override
    public List<Statistics> findMonthlyStatistics() {
        List<Object[]> results = chartRepository.findMonthlyStatistics();
        List<Statistics> statistics = new ArrayList<>();

        for (Object[] result : results) {
            String month = (String) result[0];
            int invoiceCount = ((Number) result[1]).intValue();
            int productCount = ((Number) result[2]).intValue();

            statistics.add(new Statistics(month, invoiceCount, productCount));
        }

        return statistics; // Trả về danh sách các đối tượng DTO
    }

    @Override
    public List<Statistics> findTodayStatistics() {
        List<Object[]> results = chartRepository.findTodayStatistics();
        List<Statistics> statistics = new ArrayList<>();

        for (Object[] result : results) {
            String month = (String) result[0];
            int invoiceCount = ((Number) result[1]).intValue();
            int productCount = ((Number) result[2]).intValue();

            statistics.add(new Statistics(month, invoiceCount, productCount));
        }

        return statistics;
    }

    @Override
    public List<Statistics> findLast7DaysStatistics() {
        List<Object[]> results = chartRepository.findLast7DaysStatistics();
        List<Statistics> statistics = new ArrayList<>();

        for (Object[] result : results) {
            String month = (String) result[0];
            int invoiceCount = ((Number) result[1]).intValue();
            int productCount = ((Number) result[2]).intValue();

            statistics.add(new Statistics(month, invoiceCount, productCount));
        }

        return statistics;
    }

    @Override
    public List<Statistics> getAnnualStatistics() {
        List<Object[]> results = chartRepository.getAnnualStatistics();
        List<Statistics> statistics = new ArrayList<>();

        for (Object[] result : results) {
            String month = (String) result[0];
            int invoiceCount = ((Number) result[1]).intValue();
            int productCount = ((Number) result[2]).intValue();

            statistics.add(new Statistics(month, invoiceCount, productCount));
        }

        return statistics;
    }

}

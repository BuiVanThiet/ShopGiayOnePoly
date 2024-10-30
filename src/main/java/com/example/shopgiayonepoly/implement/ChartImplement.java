package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.repositores.ChartRepository;
import com.example.shopgiayonepoly.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

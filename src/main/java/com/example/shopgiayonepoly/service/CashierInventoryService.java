package com.example.shopgiayonepoly.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface CashierInventoryService {
    List<Object[]> getAllCashierInventoryByStaff(
            String keyStaff,
            Date startDate,
            Date endDate);

    List<Object[]> getCheckCashierInventoryStaff(Integer idStaff);

    void getInsertRevenue(
            Integer idStaff,
            BigDecimal totalMoneyBill,
            BigDecimal totalMoneyReturnBill,
            BigDecimal totalMoneyExchangeBill
    );

    void getUpdateRevenue(
            Integer idStaff,
            BigDecimal totalMoneyBill,
            BigDecimal totalMoneyReturnBill,
            BigDecimal totalMoneyExchangeBill
    );

    List<Object[]> getAllCashierInventoryByIdStaff(
            Integer idStaff,
            Date startDate,
            Date endDate,
            String startTime,
            String endTime
    );
}

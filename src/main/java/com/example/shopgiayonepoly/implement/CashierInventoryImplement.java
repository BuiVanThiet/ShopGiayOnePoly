package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.repositores.CashierInventoryRepository;
import com.example.shopgiayonepoly.service.CashierInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class CashierInventoryImplement implements CashierInventoryService {
    @Autowired
    CashierInventoryRepository cashierInventoryRepository;

    @Override
    public List<Object[]> getAllCashierInventoryByStaff(
            String keyStaff,
            Date startDate,
            Date endDate) {
        return cashierInventoryRepository.getAllCashierInventoryByStaff(keyStaff,startDate,endDate);
    }

    @Override
    public  List<Object[]> getCheckCashierInventoryStaff(Integer idStaff) {
        return cashierInventoryRepository.getCheckCashierInventoryStaff(idStaff);
    }

    @Override
    public void getInsertRevenue(
            Integer idStaff,
            BigDecimal totalMoneyBill,
            BigDecimal totalMoneyReturnBill,
            BigDecimal totalMoneyExchangeBill
    ) {
        cashierInventoryRepository.getInsertRevenue(idStaff, totalMoneyBill, totalMoneyReturnBill, totalMoneyExchangeBill);
    }

    @Override
    public void getUpdateRevenue(
            Integer idStaff,
            BigDecimal totalMoneyBill,
            BigDecimal totalMoneyReturnBill,
            BigDecimal totalMoneyExchangeBill
    ) {
        cashierInventoryRepository.getUpdateRevenue(idStaff, totalMoneyBill, totalMoneyReturnBill, totalMoneyExchangeBill);
    }

    @Override
    public List<Object[]> getAllCashierInventoryByIdStaff(
            Integer idStaff,
            Date startDate,
            Date endDate,
            String startTime,
            String endTime
    ) {
        return cashierInventoryRepository.getAllCashierInventoryByIdStaff(idStaff,startDate,endDate,startTime,endTime);
    }

}

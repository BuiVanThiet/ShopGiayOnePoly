package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.Bill;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChartRepository extends JpaRepository<Bill, Integer> {
    @Query("SELECT COUNT(b) " +
            "FROM Bill b " +
            "WHERE b.status = 5 " +
            "AND MONTH(b.createDate) = MONTH(CURRENT_DATE) " +
            "AND YEAR(b.createDate) = YEAR(CURRENT_DATE)")
    long monthlyBill();

    @Query("SELECT SUM(b.totalAmount) " +
            "FROM Bill b " +
            "WHERE b.status = 5 " +
            "AND MONTH(b.createDate) = MONTH(CURRENT_DATE) " +
            "AND YEAR(b.createDate) = YEAR(CURRENT_DATE)")
    Long totalMonthlyBill();

    @Query("SELECT SUM(bd.quantity) " +
            "FROM BillDetail bd " +
            "JOIN bd.bill b " +
            "WHERE b.status = 5 " +
            "AND MONTH(b.createDate) = MONTH(CURRENT_DATE) " +
            "AND YEAR(b.createDate) = YEAR(CURRENT_DATE)")
    long totalMonthlyInvoiceProducts();

    @Query("SELECT COUNT(b) " +
            "FROM Bill b " +
            "WHERE b.status = 5 " +
            "AND CAST(b.updateDate AS DATE) = CAST(CURRENT_DATE AS DATE)")
    long billOfTheDay();

    @Query("SELECT SUM(bd.totalAmount) " +
            "FROM BillDetail bd " +
            "JOIN bd.bill b " +
            "WHERE b.status = 5 " +
            "AND CAST(b.updateDate AS date) = CAST(CURRENT_DATE AS date)")
    long totalPriceToday();
}

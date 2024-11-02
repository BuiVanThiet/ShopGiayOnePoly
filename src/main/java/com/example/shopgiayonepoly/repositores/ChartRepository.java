package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.Bill;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface ChartRepository extends JpaRepository<Bill, Integer> {
    @Query("SELECT COALESCE(COUNT(b), 0) " +  // Thay đổi COUNT(b) thành COALESCE(COUNT(b), 0)
            "FROM Bill b " +
            "WHERE b.status = 5 " +
            "AND MONTH(b.updateDate) = MONTH(CURRENT_DATE) " +
            "AND YEAR(b.updateDate) = YEAR(CURRENT_DATE)")
    long monthlyBill();

    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) " +  // Thay đổi SUM(b.totalAmount) thành COALESCE(SUM(b.totalAmount), 0)
            "FROM Bill b " +
            "WHERE b.status = 5 " +
            "AND MONTH(b.updateDate) = MONTH(CURRENT_DATE) " +
            "AND YEAR(b.updateDate) = YEAR(CURRENT_DATE)")
    Long totalMonthlyBill();

    @Query("SELECT COALESCE(SUM(bd.quantity), 0) " +  // Thay đổi SUM(bd.quantity) thành COALESCE(SUM(bd.quantity), 0)
            "FROM BillDetail bd " +
            "JOIN bd.bill b " +
            "WHERE b.status = 5 " +
            "AND MONTH(b.updateDate) = MONTH(CURRENT_DATE) " +
            "AND YEAR(b.updateDate) = YEAR(CURRENT_DATE)")
    long totalMonthlyInvoiceProducts();

    @Query("SELECT COALESCE(COUNT(b), 0) " +  // Thay đổi COUNT(b) thành COALESCE(COUNT(b), 0)
            "FROM Bill b " +
            "WHERE b.status = 5 " +
            "AND CAST(b.updateDate AS DATE) = CAST(CURRENT_DATE AS DATE)")
    long billOfTheDay();

    @Query("SELECT COALESCE(SUM(bd.totalAmount), 0) " +  // Thay đổi SUM(bd.totalAmount) thành COALESCE(SUM(bd.totalAmount), 0)
            "FROM BillDetail bd " +
            "JOIN bd.bill b " +
            "WHERE b.status = 5 " +
            "AND CAST(b.updateDate AS date) = CAST(CURRENT_DATE AS date)")
    long totalPriceToday();

    @Query("SELECT CAST(MAX(b.updateDate) AS date) " +
            "FROM Bill b " +
            "WHERE b.status = 5 " +
            "GROUP BY YEAR(b.updateDate), MONTH(b.updateDate) " +
            "ORDER BY CAST(MAX(b.updateDate) AS date)")
    List<Date> findLastBillDates();
    // Lấy ngày cuối tháng có hóa đơn
    @Query(value = "SELECT " +
            "FORMAT(MAX(b.update_date), 'dd-MM-yyyy') AS Thang, " +  // Lấy ngày cuối cùng có hóa đơn
            "COUNT(DISTINCT b.id) AS HoaDonThang, " +  // Đếm tổng số hóa đơn duy nhất cho mỗi tháng
            "SUM(bd.quantity) AS soLuong " +  // Tính tổng số lượng sản phẩm từ bill_detail cho mỗi tháng
            "FROM bill b " +
            "LEFT JOIN bill_detail bd ON b.id = bd.id_bill " +  // Sử dụng LEFT JOIN để đảm bảo tất cả hóa đơn được bao gồm
            "WHERE b.status = 5 " +  // Chỉ lấy những hóa đơn có trạng thái thanh toán thành công
            "GROUP BY FORMAT(b.update_date, 'MM-yyyy') " +  // Nhóm theo tháng và năm
            "ORDER BY MAX(b.update_date)", nativeQuery = true)
    List<Object[]> findMonthlyStatistics();
}

package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.dto.request.ProductInfoDto;
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

    @Query(value = "SELECT " +
            "FORMAT(b.update_date, 'dd-MM-yyyy') AS ngay, " +
            "COUNT(DISTINCT b.id) AS hoaDonHomNay, " +
            "SUM(bd.quantity) AS soLuong " +
            "FROM bill b " +
            "LEFT JOIN bill_detail bd ON b.id = bd.id_bill " +
            "WHERE b.status = 5 " +
            "AND CAST(b.update_date AS date) = CAST(GETDATE() AS date) " +
            "GROUP BY FORMAT(b.update_date, 'dd-MM-yyyy') " +
            "ORDER BY MAX(b.update_date)",
            nativeQuery = true)
    List<Object[]> findTodayStatistics();

    @Query(value =
            "WITH DateRange AS ( " +
                    "    SELECT CAST(GETDATE() AS DATE) AS Ngay " +
                    "    UNION ALL " +
                    "    SELECT DATEADD(DAY, -1, Ngay) FROM DateRange WHERE Ngay > DATEADD(DAY, -6, GETDATE()) " +
                    ") " +
                    "SELECT " +
                    "    FORMAT(dr.Ngay, 'dd-MM-yyyy') AS Ngay,  " +
                    "    COALESCE(COUNT(DISTINCT b.id), 0) AS HoaDon,  " +
                    "    COALESCE(SUM(bd.quantity), 0) AS SoLuong " +
                    "FROM DateRange dr " +
                    "LEFT JOIN bill b ON CAST(b.update_date AS DATE) = dr.Ngay AND b.status = 5  " +
                    "LEFT JOIN bill_detail bd ON b.id = bd.id_bill  " +
                    "GROUP BY dr.Ngay " +
                    "ORDER BY dr.Ngay DESC " +
                    "OPTION (MAXRECURSION 0)", nativeQuery = true)
    List<Object[]> findLast7DaysStatistics();

    @Query(value = "SELECT " +
            "FORMAT(MAX(b.update_date), 'dd-MM-yyyy') AS NgayCuoiNam, " +
            "COUNT(DISTINCT b.id) AS HoaDonTheoNam, " +
            "SUM(bd.quantity) AS TongSoLuong " +
            "FROM bill b " +
            "LEFT JOIN bill_detail bd ON b.id = bd.id_bill " +
            "WHERE b.status = 5 " +
            "AND YEAR(b.update_date) BETWEEN YEAR(GETDATE()) - 6 AND YEAR(GETDATE()) " +
            "GROUP BY YEAR(b.update_date)",
            nativeQuery = true)
    List<Object[]> getAnnualStatistics();

    @Query(value = """
    SELECT TOP 10 
        pd.name_product AS ProductName,
        c.name_color AS ColorName,
        s.name_size AS SizeName,
        -- Ép kiểu giá đã giảm thành DECIMAL(10,2) để giới hạn hai chữ số thập phân
        CAST(
            CASE 
                WHEN sp.discount_type = 1 AND pdt.id_sale_product IS NOT NULL THEN pdt.price * (1 - sp.discount_value / 100.0) -- Giảm theo phần trăm
                WHEN sp.discount_type = 2 AND pdt.id_sale_product IS NOT NULL THEN (pdt.price - sp.discount_value)             -- Giảm trực tiếp
                ELSE pdt.price                                                          -- Không có giảm giá
            END 
        AS DECIMAL(10,2)) AS DiscountedPrice,
        SUM(bd.quantity) AS TotalQuantity, -- Tổng quantity khi gộp các sản phẩm giống nhau
        -- Gộp các tên ảnh duy nhất
        STUFF(
            (SELECT DISTINCT ', ' + i.name_image
             FROM dbo.image i
             WHERE i.id_product = pd.id
             FOR XML PATH('')), 1, 2, '') AS ImageNames -- Xóa dấu phẩy thừa ở đầu
    FROM 
        dbo.bill b
    JOIN 
        dbo.bill_detail bd ON b.id = bd.id_bill -- Kết nối với bảng bill_detail
    JOIN 
        dbo.product_detail pdt ON bd.id_product_detail = pdt.id -- Kết nối với bảng product_detail
    JOIN 
        dbo.product pd ON pdt.id_product = pd.id -- Kết nối với bảng product
    JOIN 
        dbo.color c ON pdt.id_color = c.id -- Kết nối với bảng color
    JOIN 
        dbo.size s ON pdt.id_size = s.id -- Kết nối với bảng size
    LEFT JOIN 
        dbo.sale_product sp ON pdt.id_sale_product = sp.id -- Kết nối với bảng sale_product chỉ khi có id_sale_product
    WHERE 
        b.status = 5 -- Lọc các bill có status = 5
    GROUP BY 
        pd.name_product, 
        c.name_color, 
        s.name_size, 
        pdt.price,
        CAST(
            CASE 
                WHEN sp.discount_type = 1 AND pdt.id_sale_product IS NOT NULL THEN pdt.price * (1 - sp.discount_value / 100.0)
                WHEN sp.discount_type = 2 AND pdt.id_sale_product IS NOT NULL THEN (pdt.price - sp.discount_value)
                ELSE pdt.price 
            END 
        AS DECIMAL(10,2)),
        pd.id  -- Thêm pd.id vào GROUP BY
    ORDER BY 
        TotalQuantity DESC
""", nativeQuery = true)
    List<Object[]> getProductSales();
}

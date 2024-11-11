package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.dto.request.ProductInfoDto;
import com.example.shopgiayonepoly.entites.Bill;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT COALESCE(SUM(b.totalAmount - b.priceDiscount), 0) " +  // Thay đổi SUM(b.totalAmount) thành COALESCE(SUM(b.totalAmount), 0)
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

    @Query("SELECT COALESCE(SUM(bd.totalAmount - b.priceDiscount), 0) " +
            "FROM BillDetail bd " +
            "JOIN bd.bill b " +
            "WHERE b.status = 5 " +
            "AND CAST(b.updateDate AS date) = CAST(GETDATE() AS date)")
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
                    "ORDER BY dr.Ngay ASC " +
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
            CAST(pdt.price AS DECIMAL(10,2)) AS OriginalPrice, -- Giá gốc
            CAST(
                CASE 
                    WHEN sp.discount_type = 1 AND pdt.id_sale_product IS NOT NULL THEN pdt.price * (1 - sp.discount_value / 100.0) -- Giảm theo phần trăm
                    WHEN sp.discount_type = 2 AND pdt.id_sale_product IS NOT NULL THEN (pdt.price - sp.discount_value)             -- Giảm trực tiếp
                    ELSE pdt.price                                                          -- Không có giảm giá
                END 
            AS DECIMAL(10,2)) AS DiscountedPrice,
            SUM(bd.quantity) AS TotalQuantity, -- Tổng quantity khi gộp các sản phẩm giống nhau
            STUFF(
                (SELECT DISTINCT ', ' + i.name_image
                 FROM dbo.image i
                 WHERE i.id_product = pd.id
                 FOR XML PATH('')), 1, 2, '') AS ImageNames -- Xóa dấu phẩy thừa ở đầu
        FROM 
            dbo.bill b
        JOIN 
            dbo.bill_detail bd ON b.id = bd.id_bill
        JOIN 
            dbo.product_detail pdt ON bd.id_product_detail = pdt.id
        JOIN 
            dbo.product pd ON pdt.id_product = pd.id
        JOIN 
            dbo.color c ON pdt.id_color = c.id
        JOIN 
            dbo.size s ON pdt.id_size = s.id
        LEFT JOIN 
            dbo.sale_product sp ON pdt.id_sale_product = sp.id
        WHERE 
            b.status = 5
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
            pd.id
        ORDER BY 
            TotalQuantity DESC
        """, nativeQuery = true)
    List<Object[]> getProductSales();

    @Query(value = """ 
        WITH RankedProducts AS (
            SELECT
                pd.name_product AS ProductName,
                c.name_color AS ColorName,
                s.name_size AS SizeName,
                CAST(pdt.price AS DECIMAL(10, 2)) AS OriginalPrice,
                CAST(
                    CASE
                        WHEN sp.discount_type = 1 AND pdt.id_sale_product IS NOT NULL THEN pdt.price * (1 - sp.discount_value / 100.0)
                        WHEN sp.discount_type = 2 AND pdt.id_sale_product IS NOT NULL THEN (pdt.price - sp.discount_value)
                        ELSE pdt.price
                    END
                AS DECIMAL(10, 2)) AS DiscountedPrice,
                SUM(bd.quantity) AS TotalQuantity,
                STUFF(
                    (SELECT DISTINCT ', ' + i.name_image
                     FROM dbo.image i
                     WHERE i.id_product = pd.id
                     FOR XML PATH('')), 1, 2, '') AS ImageNames,
                ROW_NUMBER() OVER (ORDER BY SUM(bd.quantity) DESC) AS RowNum
            FROM
                dbo.bill b
            JOIN
                dbo.bill_detail bd ON b.id = bd.id_bill
            JOIN
                dbo.product_detail pdt ON bd.id_product_detail = pdt.id
            JOIN
                dbo.product pd ON pdt.id_product = pd.id
            JOIN
                dbo.color c ON pdt.id_color = c.id
            JOIN
                dbo.size s ON pdt.id_size = s.id
            LEFT JOIN
                dbo.sale_product sp ON pdt.id_sale_product = sp.id
            WHERE
                b.status = 5
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
                AS DECIMAL(10, 2)),
                pd.id
        )
        SELECT
            ProductName,
            ColorName,
            SizeName,
            OriginalPrice,
            DiscountedPrice,
            TotalQuantity,
            ImageNames
        FROM RankedProducts
        WHERE RowNum <= 10;
        
    """, countQuery = """
        WITH RankedProducts AS (
            SELECT
                pd.name_product AS ProductName,
                c.name_color AS ColorName,
                s.name_size AS SizeName,
                CAST(pdt.price AS DECIMAL(10, 2)) AS OriginalPrice,
                CAST(
                    CASE
                        WHEN sp.discount_type = 1 AND pdt.id_sale_product IS NOT NULL THEN pdt.price * (1 - sp.discount_value / 100.0)
                        WHEN sp.discount_type = 2 AND pdt.id_sale_product IS NOT NULL THEN (pdt.price - sp.discount_value)
                        ELSE pdt.price
                    END
                AS DECIMAL(10, 2)) AS DiscountedPrice,
                SUM(bd.quantity) AS TotalQuantity,
                STUFF(
                    (SELECT DISTINCT ', ' + i.name_image
                     FROM dbo.image i
                     WHERE i.id_product = pd.id
                     FOR XML PATH('')), 1, 2, '') AS ImageNames,
                ROW_NUMBER() OVER (ORDER BY SUM(bd.quantity) DESC) AS RowNum
            FROM
                dbo.bill b
            JOIN
                dbo.bill_detail bd ON b.id = bd.id_bill
            JOIN
                dbo.product_detail pdt ON bd.id_product_detail = pdt.id
            JOIN
                dbo.product pd ON pdt.id_product = pd.id
            JOIN
                dbo.color c ON pdt.id_color = c.id
            JOIN
                dbo.size s ON pdt.id_size = s.id
            LEFT JOIN
                dbo.sale_product sp ON pdt.id_sale_product = sp.id
            WHERE
                b.status = 5
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
                AS DECIMAL(10, 2)),
                pd.id
        )
        SELECT COUNT(*) 
        FROM RankedProducts
        WHERE RowNum <= 10;
    """, nativeQuery = true)

    Page<Object[]> getProductSalesPage(Pageable pageable);

    @Query(value = "SELECT " +
            "CASE " +
            "WHEN b.status IN (1, 7) THEN N'Chờ xác nhận' " +
            "WHEN b.status = 2 THEN N'Xác nhận' " +
            "WHEN b.status = 3 THEN N'Đang giao' " +
            "WHEN b.status = 4 THEN N'Đã nhận được hàng' " +
            "WHEN b.status = 5 THEN N'Hoàn thành' " +
            "WHEN b.status IN (6, 9) THEN N'Đã hủy' " +
            "WHEN b.status = 8 THEN N'Đổi-trả hàng' " +
            "ELSE N'Không xác định' " +
            "END AS statusDescription, " +
            "COUNT(b.status) AS countOfStatus " +
            "FROM Bill b " +
            "WHERE b.status <> 0 " +
            "AND MONTH(b.update_date) = MONTH(GETDATE()) " +
            "AND YEAR(b.update_date) = YEAR(GETDATE()) " +
            "GROUP BY b.status " +
            "ORDER BY countOfStatus DESC", nativeQuery = true)
    List<Object[]> findBillsWithStatusDescription();

    @Query(value = "SELECT " +
            "CASE " +
            "    WHEN b.status IN (1, 7) THEN N'Chờ xác nhận' " +
            "    WHEN b.status = 2 THEN N'Xác nhận' " +
            "    WHEN b.status = 3 THEN N'Đang giao' " +
            "    WHEN b.status = 4 THEN N'Đã nhận được hàng' " +
            "    WHEN b.status = 5 THEN N'Hoàn thành' " +
            "    WHEN b.status IN (6, 9) THEN N'Đã hủy' " +
            "    WHEN b.status = 8 THEN N'Đổi-trả hàng' " +
            "    ELSE N'Không xác định' " +
            "END AS statusDescription, " +
            "COUNT(b.status) AS countOfStatus " +
            "FROM Bill b " +
            "WHERE b.status <> 0 " +
            "AND CONVERT(date, b.update_date) = CONVERT(date, GETDATE()) " + // Lọc theo ngày hôm nay
            "GROUP BY b.status " +
            "ORDER BY countOfStatus DESC", nativeQuery = true)
    List<Object[]> getStatusCountForToday();

    @Query(value = "SELECT " +
            "CASE " +
            "    WHEN b.status IN (1, 7) THEN N'Chờ xác nhận' " +
            "    WHEN b.status = 2 THEN N'Xác nhận' " +
            "    WHEN b.status = 3 THEN N'Đang giao' " +
            "    WHEN b.status = 4 THEN N'Đã nhận được hàng' " +
            "    WHEN b.status = 5 THEN N'Hoàn thành' " +
            "    WHEN b.status IN (6, 9) THEN N'Đã hủy' " +
            "    WHEN b.status = 8 THEN N'Đổi-trả hàng' " +
            "    ELSE N'Không xác định' " +
            "END AS statusDescription, " +
            "COUNT(b.status) AS countOfStatus " +
            "FROM Bill b " +
            "WHERE b.status <> 0 " +
            "AND b.update_date >= DATEADD(DAY, -7, GETDATE()) " +  // Lọc theo 7 ngày gần nhất
            "GROUP BY b.status " +
            "ORDER BY countOfStatus DESC", nativeQuery = true)
    List<Object[]> findStatusCountsForLast7Days();

    @Query(value = "SELECT " +
            "    CASE " +
            "        WHEN b.status IN (1, 7) THEN N'Chờ xác nhận' " +
            "        WHEN b.status = 2 THEN N'Xác nhận' " +
            "        WHEN b.status = 3 THEN N'Đang giao' " +
            "        WHEN b.status = 4 THEN N'Đã nhận được hàng' " +
            "        WHEN b.status = 5 THEN N'Hoàn thành' " +
            "        WHEN b.status IN (6, 9) THEN N'Đã hủy' " +
            "        WHEN b.status = 8 THEN N'Đổi-trả hàng' " +
            "        ELSE N'Không xác định' " +
            "    END AS statusDescription, " +
            "    COUNT(b.status) AS countOfStatus " +
            "FROM Bill b " +
            "WHERE b.status <> 0 " +
            "    AND YEAR(b.update_date) = YEAR(GETDATE()) " +  // Lọc theo năm nay
            "GROUP BY b.status " +
            "ORDER BY countOfStatus DESC", nativeQuery = true)
    List<Object[]> countStatusByYear();

}

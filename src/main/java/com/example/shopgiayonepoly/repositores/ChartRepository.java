package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.dto.request.ProductInfoDto;
import com.example.shopgiayonepoly.entites.Bill;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface ChartRepository extends JpaRepository<Bill, Integer> {
    @Query("SELECT COALESCE(COUNT(b), 0) " +  // Thay đổi COUNT(b) thành COALESCE(COUNT(b), 0)
            "FROM Bill b " +
            "WHERE b.status IN (5, 8) " +
            "AND MONTH(b.updateDate) = MONTH(CURRENT_DATE) " +
            "AND YEAR(b.updateDate) = YEAR(CURRENT_DATE)")
    long monthlyBill();

    @Query(value = """
        SELECT 
            CASE 
                WHEN SUM(b.total_amount - b.price_discount) - 
                     SUM(ex.customer_refund - ex.exchange_and_return_fee - ex.customer_payment + ex.discounted_amount) < 0 
                THEN 0
                ELSE SUM(b.total_amount - b.price_discount) - 
                     SUM(ex.customer_refund - ex.exchange_and_return_fee - ex.customer_payment + ex.discounted_amount)
            END AS result
        FROM Bill b
        LEFT JOIN return_bill_exchange_bill ex ON b.id = ex.id_bill
        WHERE b.status IN (5, 8)
        AND MONTH(b.update_date) = MONTH(CURRENT_TIMESTAMP)
        AND YEAR(b.update_date) = YEAR(CURRENT_TIMESTAMP)
    """, nativeQuery = true)
    Long totalMonthlyBill();

    @Query(value = "SELECT " +
            "COALESCE(SUM(CASE WHEN b.status = 5 THEN bd.quantity ELSE 0 END), 0) " +
            " + COALESCE(SUM(CASE WHEN ebd2.id_exchang_bill IS NOT NULL THEN ebd2.quantity_exchange ELSE 0 END), 0) " +
            " + COALESCE(SUM(CASE WHEN b2.status = 8 THEN bd2.quantity ELSE 0 END), 0) " +
            " - COALESCE(SUM(CASE WHEN rbd2.id_return_bill IS NOT NULL THEN rbd2.quantity_return ELSE 0 END), 0) " +
            "FROM Bill b " +
            "LEFT JOIN bill_detail bd ON bd.id_bill = b.id " +
            "LEFT JOIN Bill b2 ON b2.id = bd.id_bill " +
            "LEFT JOIN bill_detail bd2 ON bd2.id_bill = b2.id " +
            "LEFT JOIN return_bill_exchange_bill rbeb ON rbeb.id_bill = b.id " +
            "LEFT JOIN exchange_bill_detail ebd2 ON ebd2.id_exchang_bill = rbeb.id " +
            "LEFT JOIN return_bill_detail rbd2 ON rbd2.id_return_bill = rbeb.id " +
            "WHERE b.status IN (5, 8) " +
            "AND MONTH(b.update_date) = MONTH(GETDATE()) " +
            "AND YEAR(b.update_date) = YEAR(GETDATE())",
            nativeQuery = true)
    long totalMonthlyInvoiceProducts();

    @Query("SELECT COALESCE(COUNT(b), 0) " +  // Thay đổi COUNT(b) thành COALESCE(COUNT(b), 0)
            "FROM Bill b " +
            "WHERE b.status IN(5,8) " +
            "AND CAST(b.updateDate AS DATE) = CAST(CURRENT_DATE AS DATE)")
    long billOfTheDay();

    @Query(value = """
        SELECT 
            CASE 
                WHEN SUM(b.total_amount - b.price_discount) - 
                     SUM(ex.customer_refund - ex.exchange_and_return_fee - ex.customer_payment + ex.discounted_amount) < 0 
                THEN 0
                ELSE SUM(b.total_amount - b.price_discount) - 
                     SUM(ex.customer_refund - ex.exchange_and_return_fee - ex.customer_payment + ex.discounted_amount)
            END AS result
        FROM Bill b
        LEFT JOIN return_bill_exchange_bill ex ON b.id = ex.id_bill
        WHERE b.status IN (5, 8)
        AND CONVERT(date, b.update_date) = CONVERT(date, GETDATE())
    """, nativeQuery = true)
    long totalPriceToday();

    @Query("SELECT CAST(MAX(b.updateDate) AS date) " +
            "FROM Bill b " +
            "WHERE b.status IN(5,8) " +
            "GROUP BY YEAR(b.updateDate), MONTH(b.updateDate) " +
            "ORDER BY CAST(MAX(b.updateDate) AS date)")
    List<Date> findLastBillDates();
    // Lấy ngày cuối tháng có hóa đơn
    @Query(value = "SELECT " +
            "FORMAT(MAX(b.update_date), 'dd-MM-yyyy') AS Thang, " +
            "COUNT(DISTINCT b.id) AS HoaDonThang, " +
            "COALESCE(SUM(CASE WHEN b.status = 5 THEN bd.quantity ELSE 0 END), 0) " +
            " + COALESCE(SUM(CASE WHEN ebd2.id_exchang_bill IS NOT NULL THEN ebd2.quantity_exchange ELSE 0 END), 0) " +
            " + COALESCE(SUM(CASE WHEN b2.status = 8 THEN bd2.quantity ELSE 0 END), 0) " +
            " - COALESCE(SUM(CASE WHEN rbd2.id_return_bill IS NOT NULL THEN rbd2.quantity_return ELSE 0 END), 0) AS soLuong " +
            "FROM Bill b " +
            "LEFT JOIN bill_detail bd ON bd.id_bill = b.id " +
            "LEFT JOIN Bill b2 ON b2.id = bd.id_bill " +
            "LEFT JOIN bill_detail bd2 ON bd2.id_bill = b2.id " +
            "LEFT JOIN return_bill_exchange_bill rbeb ON rbeb.id_bill = b.id " +
            "LEFT JOIN exchange_bill_detail ebd2 ON ebd2.id_exchang_bill = rbeb.id " +
            "LEFT JOIN return_bill_detail rbd2 ON rbd2.id_return_bill = rbeb.id " +
            "WHERE b.status IN(5, 8) " +
            "AND YEAR(b.update_date) = YEAR(GETDATE()) " +
            "GROUP BY FORMAT(b.update_date, 'yyyyMM') " +
            "ORDER BY MAX(b.update_date) ASC",
            nativeQuery = true)
    List<Object[]> findMonthlyStatistics();

    @Query(value = "SELECT " +
            "FORMAT(b.update_date, 'dd-MM-yyyy') AS ngay, " +
            "COUNT(DISTINCT b.id) AS hoaDonHomNay, " +
            "COALESCE(SUM(CASE WHEN b.status = 5 THEN bd.quantity ELSE 0 END), 0) " +
            " + COALESCE(SUM(CASE WHEN ebd2.id_exchang_bill IS NOT NULL THEN ebd2.quantity_exchange ELSE 0 END), 0) " +
            " + COALESCE(SUM(CASE WHEN b2.status = 8 THEN bd2.quantity ELSE 0 END), 0) " +
            " - COALESCE(SUM(CASE WHEN rbd2.id_return_bill IS NOT NULL THEN rbd2.quantity_return ELSE 0 END), 0) AS soLuong " +
            "FROM Bill b " +
            "LEFT JOIN bill_detail bd ON bd.id_bill = b.id " +
            "LEFT JOIN Bill b2 ON b2.id = bd.id_bill " +
            "LEFT JOIN bill_detail bd2 ON bd2.id_bill = b2.id " +
            "LEFT JOIN return_bill_exchange_bill rbeb ON rbeb.id_bill = b.id " +
            "LEFT JOIN exchange_bill_detail ebd2 ON ebd2.id_exchang_bill = rbeb.id " +
            "LEFT JOIN return_bill_detail rbd2 ON rbd2.id_return_bill = rbeb.id " +
            "WHERE b.status IN (5, 8) " +
            "AND CAST(b.update_date AS DATE) = CAST(GETDATE() AS DATE) " +
            "GROUP BY FORMAT(b.update_date, 'dd-MM-yyyy') " +
            "ORDER BY MAX(b.update_date)",
            nativeQuery = true)
    List<Object[]> findTodayStatistics();

    @Query(value = """
        WITH DateRange AS (
            SELECT CAST(GETDATE() AS DATE) AS Ngay
            UNION ALL
            SELECT DATEADD(DAY, -1, Ngay) 
            FROM DateRange 
            WHERE Ngay > DATEADD(DAY, -6, GETDATE())
        )
        SELECT 
            FORMAT(dr.Ngay, 'dd-MM-yyyy') AS Ngay, 
            COALESCE(COUNT(DISTINCT b.id), 0) AS HoaDon, 
            COALESCE(SUM(CASE 
                WHEN b.status = 5 THEN bd.quantity 
                ELSE 0 
            END), 0)
            + COALESCE(SUM(CASE 
                WHEN ebd2.id_exchang_bill IS NOT NULL THEN ebd2.quantity_exchange 
                ELSE 0 
            END), 0)
            + COALESCE(SUM(CASE 
                WHEN b2.status = 8 THEN bd2.quantity 
                ELSE 0 
            END), 0)
            - COALESCE(SUM(CASE 
                WHEN rbd2.id_return_bill IS NOT NULL THEN rbd2.quantity_return 
                ELSE 0 
            END), 0) AS SoLuong
        FROM DateRange dr
        LEFT JOIN bill b 
            ON CAST(b.update_date AS DATE) = dr.Ngay 
            AND b.status IN (5, 8)
        LEFT JOIN bill_detail bd 
            ON b.id = bd.id_bill
        LEFT JOIN return_bill_exchange_bill rbeb 
            ON rbeb.id_bill = b.id
        LEFT JOIN exchange_bill_detail ebd2 
            ON ebd2.id_exchang_bill = rbeb.id
        LEFT JOIN return_bill_detail rbd2 
            ON rbd2.id_return_bill = rbeb.id
        LEFT JOIN bill_detail bd2 
            ON bd2.id_bill = b.id
        LEFT JOIN Bill b2 
            ON b2.id = bd2.id_bill
        GROUP BY dr.Ngay
        ORDER BY dr.Ngay ASC
        OPTION (MAXRECURSION 0);
        """, nativeQuery = true)
    List<Object[]> findLast7DaysStatistics();

    @Query(value = """
        SELECT 
            FORMAT(MAX(b.update_date), 'dd-MM-yyyy') AS NgayCuoiNam,
            COUNT(DISTINCT b.id) AS HoaDonTheoNam,
            COALESCE(SUM(CASE 
                WHEN b.status = 5 THEN bd.quantity 
                ELSE 0 
            END), 0)
            + COALESCE(SUM(CASE 
                WHEN ebd2.id_exchang_bill IS NOT NULL THEN ebd2.quantity_exchange 
                ELSE 0 
            END), 0)
            + COALESCE(SUM(CASE 
                WHEN b2.status = 8 THEN bd2.quantity 
                ELSE 0 
            END), 0)
            - COALESCE(SUM(CASE 
                WHEN rbd2.id_return_bill IS NOT NULL THEN rbd2.quantity_return 
                ELSE 0 
            END), 0) AS TongSoLuong
        FROM bill b
        LEFT JOIN bill_detail bd 
            ON b.id = bd.id_bill
        LEFT JOIN return_bill_exchange_bill rbeb 
            ON rbeb.id_bill = b.id
        LEFT JOIN exchange_bill_detail ebd2 
            ON ebd2.id_exchang_bill = rbeb.id
        LEFT JOIN return_bill_detail rbd2 
            ON rbd2.id_return_bill = rbeb.id
        LEFT JOIN bill_detail bd2 
            ON bd2.id_bill = b.id
        LEFT JOIN bill b2 
            ON b2.id = bd2.id_bill
        WHERE b.status IN (5, 8)
        AND YEAR(b.update_date) BETWEEN YEAR(GETDATE()) - 6 AND YEAR(GETDATE())
        GROUP BY YEAR(b.update_date)
        ORDER BY YEAR(b.update_date) ASC;
        """, nativeQuery = true)
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
                AND CAST(b.update_date AS DATE) BETWEEN :startDate AND :endDate
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
                AND CAST(b.update_date AS DATE) BETWEEN :startDate AND :endDate
            
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

    Page<Object[]> getProductSalesPageByDateRange(Pageable pageable, @Param("startDate") String startDate, @Param("endDate") String endDate);

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
            "AND CAST(b.update_date AS DATE) BETWEEN :startDate AND :endDate " +  // Lọc theo khoảng thời gian nhập từ HTML
            "GROUP BY b.status " +
            "ORDER BY countOfStatus DESC", nativeQuery = true)
    List<Object[]> getBillStatisticsByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);


    @Query(value =
            "WITH DateRange AS ( " +
                    "    SELECT CAST(:startDate AS DATE) AS Ngay " +
                    "    UNION ALL " +
                    "    SELECT DATEADD(DAY, 1, Ngay) " +
                    "    FROM DateRange " +
                    "    WHERE Ngay < CAST(:endDate AS DATE) " + // Sử dụng ngày kết thúc làm tham số
                    ") " +
                    "SELECT " +
                    "    FORMAT(dr.Ngay, 'dd-MM-yyyy') AS Ngay, " +
                    "    COALESCE(COUNT(DISTINCT b.id), 0) AS HoaDon, " +
                    "    COALESCE(SUM(bd.quantity), 0) AS SoLuong " +
                    "FROM DateRange dr " +
                    "LEFT JOIN bill b ON CAST(b.update_date AS DATE) = dr.Ngay AND b.status IN(5,8) " +
                    "LEFT JOIN bill_detail bd ON b.id = bd.id_bill " +
                    "GROUP BY dr.Ngay " +
                    "ORDER BY dr.Ngay ASC " +
                    "OPTION (MAXRECURSION 0)", nativeQuery = true)
    List<Object[]> findStatisticsByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);

}

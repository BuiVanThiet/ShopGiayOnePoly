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
    @Query(value = """
        SELECT
            COALESCE(
                SUM(CASE
                        WHEN b.status = 5 AND MONTH(b.update_date) = MONTH(GETDATE()) AND YEAR(b.update_date) = YEAR(GETDATE()) THEN 1
                        ELSE 0
                    END) +
                SUM(CASE
                        WHEN b.status = 8 AND MONTH(b.create_date) = MONTH(GETDATE()) AND YEAR(b.create_date) = YEAR(GETDATE()) THEN 1
                        ELSE 0
                    END) +
                SUM(CASE
                        WHEN b.status = 8 AND MONTH(b.update_date) = MONTH(GETDATE()) AND YEAR(b.update_date) = YEAR(GETDATE()) THEN 1
                        ELSE 0
                    END) +
                SUM(CASE
                        WHEN b.status = 9 AND b.bill_type = 1 AND MONTH(b.create_date) = MONTH(GETDATE()) AND YEAR(b.create_date) = YEAR(GETDATE()) THEN 1
                        ELSE 0
                    END) +
                SUM(CASE
                        WHEN b.status = 9 AND b.bill_type = 1 AND MONTH(b.update_date) = MONTH(GETDATE()) AND YEAR(b.update_date) = YEAR(GETDATE()) THEN 1
                        ELSE 0
                    END) +
                SUM(CASE
                        WHEN b.status = 9 AND b.bill_type = 2 AND MONTH(b.update_date) = MONTH(GETDATE()) AND YEAR(b.update_date) = YEAR(GETDATE()) THEN 1
                        ELSE 0
                    END),
                0
            ) AS TotalHoaDon
        FROM
            Bill b
        WHERE
            b.status IN (5, 8, 9);
        
        """,nativeQuery = true)
    Long monthlyBill();

    @Query(value = """
        SELECT
           COALESCE(SUM(CASE
                           WHEN b.status IN (5, 8, 9) THEN (b.total_amount - b.price_discount)
                           ELSE 0
                       END), 0)
           -
          
            COALESCE(SUM(CASE
                           WHEN ex.status = 1 THEN
                               ex.customer_refund
                               - ex.exchange_and_return_fee
                               - ex.customer_payment
                               + ex.discounted_amount
                           ELSE 0
                       END), 0) AS result
       FROM
           Bill b
       LEFT JOIN
           return_bill_exchange_bill ex
       ON
           b.id = ex.id_bill
       WHERE
           b.status IN (5, 8, 9)
           AND MONTH(b.update_date) = MONTH(GETDATE())
           AND YEAR(b.update_date) = YEAR(GETDATE());             
    """, nativeQuery = true)
    Long totalMonthlyBill();

    @Query(value = """
        SELECT
            ISNULL((
                    COALESCE(SUM(CASE WHEN b.status IN (5, 8, 9) THEN bd.quantity ELSE 0 END), 0)  -- Tổng số lượng từ bảng hóa đơn chi tiết
                    + COALESCE(SUM(CASE WHEN ebd2.quantity_exchange IS NOT NULL THEN ebd2.quantity_exchange ELSE 0 END), 0)  -- Tổng số lượng đổi từ bảng chi tiết hóa đơn đổi
                    - COALESCE(SUM(CASE WHEN rbeb.status = 1 THEN rbd2.quantity_return ELSE 0 END), 0)  -- Tổng số lượng trả lại từ bảng chi tiết hóa đơn trả lại
                ), 0) AS SoLuong
        FROM dbo.Bill b
        LEFT JOIN dbo.bill_detail bd ON bd.id_bill = b.id
        LEFT JOIN dbo.return_bill_exchange_bill rbeb ON rbeb.id_bill = b.id
        LEFT JOIN dbo.exchange_bill_detail ebd2 ON ebd2.id_exchang_bill = rbeb.id
        LEFT JOIN dbo.return_bill_detail rbd2 ON rbd2.id_return_bill = rbeb.id
        WHERE
            b.status IN (5, 8, 9)
            AND MONTH(b.update_date) = MONTH(GETDATE())
            AND YEAR(b.update_date) = YEAR(GETDATE());
        
        """,
            nativeQuery = true)
    Long totalMonthlyInvoiceProducts();

    @Query(value = """
        SELECT
            COALESCE(
                SUM(CASE
                        WHEN b.status = 5 AND CAST(b.update_date AS DATE) = CAST(GETDATE() AS DATE) THEN 1
                        ELSE 0
                    END) +
                SUM(CASE
                        WHEN b.status = 8 AND CAST(b.create_date AS DATE) = CAST(GETDATE() AS DATE) THEN 1
                        ELSE 0
                    END) +
                SUM(CASE
                        WHEN b.status = 8 AND CAST(b.update_date AS DATE) = CAST(GETDATE() AS DATE) THEN 1
                        ELSE 0
                    END) +
                SUM(CASE
                        WHEN b.status = 9 AND b.bill_type = 1 AND CAST(b.create_date AS DATE) = CAST(GETDATE() AS DATE) THEN 1
                        ELSE 0
                    END) +
                SUM(CASE
                        WHEN b.status = 9 AND b.bill_type = 1 AND CAST(b.update_date AS DATE) = CAST(GETDATE() AS DATE) THEN 1
                        ELSE 0
                    END) +
                SUM(CASE
                        WHEN b.status = 9 AND b.bill_type = 2 AND CAST(b.update_date AS DATE) = CAST(GETDATE() AS DATE) THEN 1
                        ELSE 0
                    END),
                0
            ) AS TotalHoaDon
        FROM
            Bill b
        WHERE
            b.status IN (5, 8, 9);
        
    """, nativeQuery = true)
    Long billOfTheDay();

    @Query(value = """
        SELECT
            COALESCE(SUM(CASE
                            WHEN b.status IN (5, 8, 9) THEN (b.total_amount - b.price_discount)
                            ELSE 0
                        END), 0)
            -
            COALESCE(SUM(CASE
                        WHEN ex.status = 1 THEN
                            ex.customer_refund
                            - ex.exchange_and_return_fee
                            - ex.customer_payment
                            + ex.discounted_amount
                        ELSE 0
                    END), 0) AS result
        FROM
            Bill b
        LEFT JOIN
            return_bill_exchange_bill ex
        ON
            b.id = ex.id_bill
        WHERE
            b.status IN (5, 8, 9)
            AND CAST(b.update_date AS DATE) = CAST(GETDATE() AS DATE);  -- Lọc theo ngày hôm nay
        
        """, nativeQuery = true)
    Long totalPriceToday();

    @Query("SELECT CAST(MAX(b.updateDate) AS date) " +
            "FROM Bill b " +
            "WHERE b.status IN(5,8) " +
            "GROUP BY YEAR(b.updateDate), MONTH(b.updateDate) " +
            "ORDER BY CAST(MAX(b.updateDate) AS date)")
    List<Date> findLastBillDates();
    // Lấy ngày cuối tháng có hóa đơn
    @Query(value = """
        SELECT
        	 FORMAT(MAX(b.update_date), 'dd-MM-yyyy') AS Thang,
            -- Tính toán số lượng hóa đơn
            SUM(CASE WHEN b.status = 5 AND MONTH(b.update_date) = MONTH(GETDATE()) AND YEAR(b.update_date) = YEAR(GETDATE()) THEN 1 ELSE 0 END) +
            SUM(CASE WHEN b.status = 8 AND MONTH(b.create_date) = MONTH(GETDATE()) AND YEAR(b.create_date) = YEAR(GETDATE()) THEN 1 ELSE 0 END) +
            SUM(CASE WHEN b.status = 8 AND MONTH(b.update_date) = MONTH(GETDATE()) AND YEAR(b.update_date) = YEAR(GETDATE()) THEN 1 ELSE 0 END) + 
            SUM(CASE WHEN b.status = 9 AND b.bill_type = 1 AND MONTH(b.create_date) = MONTH(GETDATE()) AND YEAR(b.create_date) = YEAR(GETDATE()) THEN 1 ELSE 0 END) +
            SUM(CASE WHEN b.status = 9 AND b.bill_type = 1 AND MONTH(b.update_date) = MONTH(GETDATE()) AND YEAR(b.update_date) = YEAR(GETDATE()) THEN 1 ELSE 0 END) + 
            SUM(CASE WHEN b.status = 9 AND b.bill_type = 2 AND MONTH(b.update_date) = MONTH(GETDATE()) AND YEAR(b.update_date) = YEAR(GETDATE()) THEN 1 ELSE 0 END) AS TotalHoaDon,
        
            -- Tính toán tổng số lượng từ các bảng chi tiết và hóa đơn trả lại
            ISNULL((
                COALESCE(SUM(CASE WHEN b.status IN (5, 8, 9) THEN bd.quantity ELSE 0 END), 0)  -- Tổng số lượng từ bảng hóa đơn chi tiết
                + COALESCE(SUM(CASE WHEN ebd2.quantity_exchange IS NOT NULL THEN ebd2.quantity_exchange ELSE 0 END), 0)  -- Tổng số lượng đổi từ bảng chi tiết hóa đơn đổi
                - COALESCE(SUM(CASE WHEN rbeb.status = 1 THEN rbd2.quantity_return ELSE 0 END), 0)  -- Tổng số lượng trả lại từ bảng chi tiết hóa đơn trả lại
            ), 0) AS SoLuong
        FROM
            dbo.Bill b
        LEFT JOIN
            dbo.bill_detail bd ON bd.id_bill = b.id
        LEFT JOIN
            dbo.return_bill_exchange_bill rbeb ON rbeb.id_bill = b.id
        LEFT JOIN
            dbo.exchange_bill_detail ebd2 ON ebd2.id_exchang_bill = rbeb.id
        LEFT JOIN
            dbo.return_bill_detail rbd2 ON rbd2.id_return_bill = rbeb.id
        WHERE
            b.status IN (5, 8, 9)  -- Điều kiện trạng thái hóa đơn
           AND (
                CAST(b.update_date AS DATE) >= DATEADD(MONTH, -6, GETDATE())
                OR CAST(b.create_date AS DATE) >= DATEADD(MONTH, -6, GETDATE())
            )
        GROUP BY
            FORMAT(b.update_date, 'MM-yyyy') -- Nhóm theo tháng và năm
        ORDER BY
            FORMAT(b.update_date, 'MM-yyyy') ASC;
    """,nativeQuery = true)
    List<Object[]> findMonthlyStatistics();

    @Query(value = """
            SELECT
                FORMAT(MAX(b.update_date), 'dd-MM-yyyy') AS Thang,
                -- Tính toán số lượng hóa đơn
                SUM(CASE WHEN b.status = 5 AND CAST(b.update_date AS DATE) = CAST(GETDATE() AS DATE) THEN 1 ELSE 0 END) +
                SUM(CASE WHEN b.status = 8 AND CAST(b.create_date AS DATE) = CAST(GETDATE() AS DATE) THEN 1 ELSE 0 END) +
                SUM(CASE WHEN b.status = 8 AND CAST(b.update_date AS DATE) = CAST(GETDATE() AS DATE) THEN 1 ELSE 0 END) + 
                SUM(CASE WHEN b.status = 9 AND b.bill_type = 1 AND CAST(b.create_date AS DATE) = CAST(GETDATE() AS DATE) THEN 1 ELSE 0 END) +
                SUM(CASE WHEN b.status = 9 AND b.bill_type = 1 AND CAST(b.update_date AS DATE) = CAST(GETDATE() AS DATE) THEN 1 ELSE 0 END) + 
                SUM(CASE WHEN b.status = 9 AND b.bill_type = 2 AND CAST(b.update_date AS DATE) = CAST(GETDATE() AS DATE) THEN 1 ELSE 0 END) AS TotalHoaDon,
            
                -- Tính toán tổng số lượng từ các bảng chi tiết và hóa đơn trả lại
                ISNULL((
                    COALESCE(SUM(CASE WHEN b.status IN (5, 8, 9) THEN bd.quantity ELSE 0 END), 0)  -- Tổng số lượng từ bảng hóa đơn chi tiết
                    + COALESCE(SUM(CASE WHEN ebd2.quantity_exchange IS NOT NULL THEN ebd2.quantity_exchange ELSE 0 END), 0)  -- Tổng số lượng đổi từ bảng chi tiết hóa đơn đổi
                    - COALESCE(SUM(CASE WHEN rbeb.status = 1 THEN rbd2.quantity_return ELSE 0 END), 0)  -- Tổng số lượng trả lại từ bảng chi tiết hóa đơn trả lại
                ), 0) AS SoLuong
            FROM
                dbo.Bill b
            LEFT JOIN
                dbo.bill_detail bd ON bd.id_bill = b.id
            LEFT JOIN
                dbo.return_bill_exchange_bill rbeb ON rbeb.id_bill = b.id
            LEFT JOIN
                dbo.exchange_bill_detail ebd2 ON ebd2.id_exchang_bill = rbeb.id
            LEFT JOIN
                dbo.return_bill_detail rbd2 ON rbd2.id_return_bill = rbeb.id
            WHERE
                b.status IN (5, 8, 9)  -- Điều kiện trạng thái hóa đơn
                AND (
                    CAST(b.update_date AS DATE) = CAST(GETDATE() AS DATE)
                    OR CAST(b.create_date AS DATE) = CAST(GETDATE() AS DATE)
                )
            GROUP BY
                FORMAT(b.update_date, 'MM-yyyy') -- Nhóm theo tháng và năm
            ORDER BY
                FORMAT(b.update_date, 'MM-yyyy') ASC;
            """, nativeQuery = true)
    List<Object[]> findTodayStatistics();

    @Query(value = """
        WITH DateRange AS (
            -- Tạo danh sách các ngày trong 7 ngày gần đây
            SELECT CAST(DATEADD(DAY, -6, GETDATE()) AS DATE) AS DateValue
            UNION ALL
            SELECT DATEADD(DAY, 1, DateValue)
            FROM DateRange
            WHERE DateValue < CAST(GETDATE() AS DATE)
        )
        SELECT
            FORMAT(dr.DateValue, 'dd-MM-yyyy') AS Ngay,
                SUM(CASE WHEN b.status = 5 AND CAST(b.update_date AS DATE) = dr.DateValue THEN 1 ELSE 0 END) +
                SUM(CASE WHEN b.status = 8 AND CAST(b.create_date AS DATE) = dr.DateValue THEN 1 ELSE 0 END) +
                SUM(CASE WHEN b.status = 8 AND CAST(b.update_date AS DATE) = dr.DateValue THEN 1 ELSE 0 END) + 
                SUM(CASE WHEN b.status = 9 AND b.bill_type = 1 AND CAST(b.create_date AS DATE) = dr.DateValue THEN 1 ELSE 0 END) +
                SUM(CASE WHEN b.status = 9 AND b.bill_type = 1 AND CAST(b.update_date AS DATE) = dr.DateValue THEN 1 ELSE 0 END) + 
                SUM(CASE WHEN b.status = 9 AND b.bill_type = 2 AND CAST(b.update_date AS DATE) = dr.DateValue THEN 1 ELSE 0 END) AS TotalHoaDon,
            
                -- Tính toán tổng số lượng từ các bảng chi tiết và hóa đơn trả lại
                ISNULL((
                    COALESCE(SUM(CASE WHEN b.status IN (5, 8, 9) THEN bd.quantity ELSE 0 END), 0)  -- Tổng số lượng từ bảng hóa đơn chi tiết
                    + COALESCE(SUM(CASE WHEN ebd2.quantity_exchange IS NOT NULL THEN ebd2.quantity_exchange ELSE 0 END), 0)  -- Tổng số lượng đổi từ bảng chi tiết hóa đơn đổi
                    - COALESCE(SUM(CASE WHEN rbeb.status = 1 THEN rbd2.quantity_return ELSE 0 END), 0)  -- Tổng số lượng trả lại từ bảng chi tiết hóa đơn trả lại
                ), 0) AS SoLuong
        FROM
            DateRange dr
        LEFT JOIN dbo.Bill b
            ON CAST(b.update_date AS DATE) = dr.DateValue
            OR CAST(b.create_date AS DATE) = dr.DateValue
        LEFT JOIN dbo.bill_detail bd
            ON bd.id_bill = b.id
        LEFT JOIN dbo.return_bill_exchange_bill rbeb
            ON rbeb.id_bill = b.id
        LEFT JOIN dbo.exchange_bill_detail ebd2
            ON ebd2.id_exchang_bill = rbeb.id
        LEFT JOIN dbo.return_bill_detail rbd2
            ON rbd2.id_return_bill = rbeb.id
        GROUP BY
            dr.DateValue
        ORDER BY
            dr.DateValue ASC;                      
        """, nativeQuery = true)
    List<Object[]> findLast7DaysStatistics();

    @Query(value = """
        WITH YearRange AS (
            -- Tạo danh sách các năm trong 5 năm gần đây
            SELECT CAST(DATEADD(YEAR, -4, GETDATE()) AS DATE) AS YearStart
            UNION ALL
            SELECT DATEADD(YEAR, 1, YearStart)
            FROM YearRange
            WHERE YearStart < CAST(GETDATE() AS DATE)
        )
        SELECT
            FORMAT(MAX(dr.YearStart), 'dd-MM-yyyy') AS Nam,
            SUM(CASE WHEN b.status = 5 AND YEAR(b.update_date) = YEAR(dr.YearStart) THEN 1 ELSE 0 END) +
                SUM(CASE WHEN b.status = 8 AND YEAR(b.update_date) = YEAR(dr.YearStart) THEN 1 ELSE 0 END) +
                SUM(CASE WHEN b.status = 8 AND YEAR(b.update_date) = YEAR(dr.YearStart) THEN 1 ELSE 0 END) + 
                SUM(CASE WHEN b.status = 9 AND b.bill_type = 1 AND YEAR(b.update_date) = YEAR(dr.YearStart) THEN 1 ELSE 0 END) +
                SUM(CASE WHEN b.status = 9 AND b.bill_type = 1 AND YEAR(b.update_date) = YEAR(dr.YearStart) THEN 1 ELSE 0 END) + 
                SUM(CASE WHEN b.status = 9 AND b.bill_type = 2 AND YEAR(b.update_date) = YEAR(dr.YearStart) THEN 1 ELSE 0 END) AS TotalHoaDon,
            
                -- Tính toán tổng số lượng từ các bảng chi tiết và hóa đơn trả lại
                ISNULL((
                    COALESCE(SUM(CASE WHEN b.status IN (5, 8, 9) THEN bd.quantity ELSE 0 END), 0)  -- Tổng số lượng từ bảng hóa đơn chi tiết
                    + COALESCE(SUM(CASE WHEN ebd2.quantity_exchange IS NOT NULL THEN ebd2.quantity_exchange ELSE 0 END), 0)  -- Tổng số lượng đổi từ bảng chi tiết hóa đơn đổi
                    - COALESCE(SUM(CASE WHEN rbeb.status = 1 THEN rbd2.quantity_return ELSE 0 END), 0)  -- Tổng số lượng trả lại từ bảng chi tiết hóa đơn trả lại
                ), 0) AS SoLuong
        FROM
            YearRange dr
        LEFT JOIN dbo.Bill b
            ON YEAR(b.update_date) = YEAR(dr.YearStart)
            OR YEAR(b.create_date) = YEAR(dr.YearStart)
        LEFT JOIN dbo.bill_detail bd
            ON bd.id_bill = b.id
        LEFT JOIN dbo.return_bill_exchange_bill rbeb
            ON rbeb.id_bill = b.id
        LEFT JOIN dbo.exchange_bill_detail ebd2
            ON ebd2.id_exchang_bill = rbeb.id
        LEFT JOIN dbo.return_bill_detail rbd2
            ON rbd2.id_return_bill = rbeb.id
        GROUP BY
            dr.YearStart
        HAVING
            ISNULL(
                SUM(CASE
                        WHEN b.status = 5 AND YEAR(b.update_date) = YEAR(dr.YearStart) THEN 1
                        ELSE 0
                    END) +
                SUM(CASE
                        WHEN b.status = 8 AND YEAR(b.create_date) = YEAR(dr.YearStart) THEN 1
                        ELSE 0
                    END) +
                SUM(CASE
                        WHEN b.status = 8 AND YEAR(b.update_date) = YEAR(dr.YearStart) THEN 1
                        ELSE 0
                    END) +
                SUM(CASE
                        WHEN b.status = 9 AND b.bill_type = 1 AND YEAR(b.create_date) = YEAR(dr.YearStart) THEN 1
                        ELSE 0
                    END) +
                SUM(CASE
                        WHEN b.status = 9 AND b.bill_type = 1 AND YEAR(b.update_date) = YEAR(dr.YearStart) THEN 1
                        ELSE 0
                    END) +
                SUM(CASE
                        WHEN b.status = 9 AND b.bill_type = 2 AND YEAR(b.update_date) = YEAR(dr.YearStart) THEN 1
                        ELSE 0
                    END), 0
            ) > 0
        ORDER BY
            dr.YearStart ASC;
    """, nativeQuery = true)
    List<Object[]> getAnnualStatistics();

    @Query(value = """
        SELECT TOP 3
           p.name_product AS ProductName,
           c.name_color AS ColorName,
           s.name_size AS SizeName,
           -- Giá gốc (nếu âm thì bằng 0)
           CASE
               WHEN pd.price < 0 THEN 0
               ELSE pd.price
           END AS OriginalPrice,
           -- Giá sau giảm giá (nếu âm thì bằng 0)
           CASE
               WHEN sp.discount_type = 1 AND pd.id_sale_product IS NOT NULL THEN
                   CASE
                       WHEN pd.price * (1 - sp.discount_value / 100.0) < 0 THEN 0
                       ELSE pd.price * (1 - sp.discount_value / 100.0)
                   END
               WHEN sp.discount_type = 2 AND pd.id_sale_product IS NOT NULL THEN
                   CASE
                       WHEN (pd.price - sp.discount_value) < 0 THEN 0
                       ELSE (pd.price - sp.discount_value)
                   END
               ELSE
                   CASE
                       WHEN pd.price < 0 THEN 0
                       ELSE pd.price
                   END
           END AS DiscountedPrice,
           -- Tính tổng số lượng (sau khi trả lại và đổi trả)
           ISNULL(
               (SELECT SUM(bd.quantity)
                FROM dbo.bill_detail bd
                JOIN dbo.bill b ON bd.id_bill = b.id
                WHERE bd.id_product_detail = pd.id AND b.status IN (5, 8, 9)
               ), 0
           ) +
           ISNULL(
               (SELECT SUM(ebd.quantity_exchange)
                FROM dbo.exchange_bill_detail ebd
                LEFT JOIN dbo.return_bill_exchange_bill rbe_exchange ON ebd.id_exchang_bill = rbe_exchange.id
                WHERE ebd.id_product_detail = pd.id AND rbe_exchange.status = 1
               ), 0
           ) -
           ISNULL(
               (SELECT SUM(rb.quantity_return)
                FROM dbo.return_bill_detail rb
                LEFT JOIN dbo.return_bill_exchange_bill rbe_return ON rb.id_return_bill = rbe_return.id
                WHERE rb.id_product_detail = pd.id AND rbe_return.status = 1
               ), 0
           ) AS FinalQuantity,
           -- Danh sách tên ảnh
           ISNULL(
               STUFF(
                   (SELECT DISTINCT ', ' + i.name_image
                    FROM dbo.image i
                    WHERE i.id_product = p.id
                    FOR XML PATH('')), 1, 2, ''
               ), 'Không có ảnh'
           ) AS ImageNames
       FROM
           dbo.product_detail pd
       LEFT JOIN
           dbo.bill_detail bd ON pd.id = bd.id_product_detail
       LEFT JOIN
           dbo.product p ON pd.id_product = p.id
       LEFT JOIN
           dbo.color c ON pd.id_color = c.id
       LEFT JOIN
           dbo.size s ON pd.id_size = s.id
       LEFT JOIN
           dbo.sale_product sp ON pd.id_sale_product = sp.id
       LEFT JOIN
           dbo.image i ON p.id = i.id_product
       WHERE
           pd.id IS NOT NULL -- Loại bỏ các giá trị không hợp lệ
           AND (
               EXISTS (
                   SELECT 1
                   FROM dbo.bill_detail bd
                   JOIN dbo.bill b ON bd.id_bill = b.id
                   WHERE bd.id_product_detail = pd.id AND b.status IN (5, 8, 9)
               )
               OR EXISTS (
                   SELECT 1
                   FROM dbo.exchange_bill_detail ebd
                   LEFT JOIN dbo.return_bill_exchange_bill rbe_exchange ON ebd.id_exchang_bill = rbe_exchange.id
                   WHERE ebd.id_product_detail = pd.id AND rbe_exchange.status = 1
               )
               OR EXISTS (
                   SELECT 1
                   FROM dbo.return_bill_detail rb
                   LEFT JOIN dbo.return_bill_exchange_bill rbe_return ON rb.id_return_bill = rbe_return.id
                   WHERE rb.id_product_detail = pd.id AND rbe_return.status = 1
               )
           )
       GROUP BY
           pd.id, p.name_product, c.name_color, s.name_size, pd.price, sp.discount_type, sp.discount_value, pd.id_sale_product, p.id
       ORDER BY
           FinalQuantity DESC;
        """, nativeQuery = true)
    List<Object[]> getProductSales();

    @Query(value = """
           SELECT TOP 3 
               p.name_product AS productName, 
               c.name_color AS colorName, 
               s.name_size AS sizeName,
               CASE 
                   WHEN pd.price < 0 THEN 0 
                   ELSE pd.price 
               END AS originalPrice,
               CASE 
                   WHEN sp.discount_type = 1 AND pd.id_sale_product IS NOT NULL THEN 
                       CASE 
                           WHEN pd.price * (1 - sp.discount_value / 100.0) < 0 THEN 0 
                           ELSE pd.price * (1 - sp.discount_value / 100.0) 
                       END 
                   WHEN sp.discount_type = 2 AND pd.id_sale_product IS NOT NULL THEN 
                       CASE 
                           WHEN (pd.price - sp.discount_value) < 0 THEN 0 
                           ELSE (pd.price - sp.discount_value) 
                       END 
                   ELSE 
                       CASE 
                           WHEN pd.price < 0 THEN 0 
                           ELSE pd.price 
                       END 
               END AS discountedPrice,
               ISNULL((SELECT SUM(bd.quantity) 
                       FROM dbo.bill_detail bd 
                       JOIN dbo.bill b ON bd.id_bill = b.id 
                       WHERE bd.id_product_detail = pd.id AND b.status IN (5, 8, 9) 
                         AND b.create_date BETWEEN :startDate AND :endDate), 0) + 
               ISNULL((SELECT SUM(ebd.quantity_exchange) 
                       FROM dbo.exchange_bill_detail ebd 
                       LEFT JOIN dbo.return_bill_exchange_bill rbe_exchange ON ebd.id_exchang_bill = rbe_exchange.id 
                       WHERE ebd.id_product_detail = pd.id AND rbe_exchange.status = 1 
                         AND ebd.create_date BETWEEN :startDate AND :endDate), 0) - 
               ISNULL((SELECT SUM(rb.quantity_return) 
                       FROM dbo.return_bill_detail rb 
                       LEFT JOIN dbo.return_bill_exchange_bill rbe_return ON rb.id_return_bill = rbe_return.id 
                       WHERE rb.id_product_detail = pd.id AND rbe_return.status = 1 
                         AND rb.create_date BETWEEN :startDate AND :endDate), 0) AS finalQuantity,
               ISNULL(STUFF((SELECT DISTINCT ', ' + i.name_image 
                             FROM dbo.image i 
                             WHERE i.id_product = p.id 
                             FOR XML PATH('')), 1, 2, ''), 'Không có ảnh') AS imageNames
           FROM 
               dbo.product_detail pd 
           LEFT JOIN 
               dbo.product p ON pd.id_product = p.id 
           LEFT JOIN 
               dbo.color c ON pd.id_color = c.id 
           LEFT JOIN 
               dbo.size s ON pd.id_size = s.id 
           LEFT JOIN 
               dbo.sale_product sp ON pd.id_sale_product = sp.id 
           WHERE 
               pd.id IS NOT NULL AND (
                   EXISTS (
                       SELECT 1 
                       FROM dbo.bill_detail bd 
                       JOIN dbo.bill b ON bd.id_bill = b.id 
                       WHERE bd.id_product_detail = pd.id AND b.status IN (5, 8, 9) 
                         AND b.create_date BETWEEN :startDate AND :endDate
                   ) OR EXISTS (
                       SELECT 1 
                       FROM dbo.exchange_bill_detail ebd 
                       LEFT JOIN dbo.return_bill_exchange_bill rbe_exchange ON ebd.id_exchang_bill = rbe_exchange.id 
                       WHERE ebd.id_product_detail = pd.id AND rbe_exchange.status = 1 
                         AND ebd.create_date BETWEEN :startDate AND :endDate
                   ) OR EXISTS (
                       SELECT 1 
                       FROM dbo.return_bill_detail rb 
                       LEFT JOIN dbo.return_bill_exchange_bill rbe_return ON rb.id_return_bill = rbe_return.id 
                       WHERE rb.id_product_detail = pd.id AND rbe_return.status = 1 
                         AND rb.create_date BETWEEN :startDate AND :endDate
                   )
               )
           ORDER BY 
               finalQuantity DESC
           """, nativeQuery = true)
    List<Object[]> findTopProductsByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);

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


    @Query(value = """
        WITH DateRange AS (
            -- Tạo danh sách ngày trong khoảng từ startDate đến endDate (hoặc startDate + 99 ngày nếu khoảng cách lớn hơn 99 ngày)
            SELECT CAST(:startDate AS DATE) AS DateValue
            UNION ALL
            SELECT DATEADD(DAY, 1, DateValue)
            FROM DateRange
            WHERE DateValue <
                CASE
                    WHEN DATEDIFF(DAY, CAST(:startDate AS DATE), CAST(:endDate AS DATE)) > 99
                    THEN DATEADD(DAY, 99, CAST(:startDate AS DATE))
                    ELSE CAST(:endDate AS DATE)
                END
        )
        SELECT
            FORMAT(dr.DateValue, 'dd-MM-yyyy') AS Ngay,
            ISNULL(
                SUM(CASE
                        WHEN b.status = 5 AND CAST(b.update_date AS DATE) = dr.DateValue THEN 1
                        ELSE 0
                    END) +
                SUM(CASE
                        WHEN b.status = 8 AND CAST(b.create_date AS DATE) = dr.DateValue THEN 1
                        ELSE 0
                    END) +
                SUM(CASE
                        WHEN b.status = 8 AND CAST(b.update_date AS DATE) = dr.DateValue THEN 1
                        ELSE 0
                    END) +
                SUM(CASE
                        WHEN b.status = 9 AND b.bill_type = 1 AND CAST(b.create_date AS DATE) = dr.DateValue THEN 1
                        ELSE 0
                    END) +
                SUM(CASE
                        WHEN b.status = 9 AND b.bill_type = 1 AND CAST(b.update_date AS DATE) = dr.DateValue THEN 1
                        ELSE 0
                    END) +
                SUM(CASE
                        WHEN b.status = 9 AND b.bill_type = 2 AND CAST(b.update_date AS DATE) = dr.DateValue THEN 1
                        ELSE 0
                    END), 0
            ) AS HoaDonNgay,
            ISNULL((
                COALESCE(SUM(CASE WHEN b.status IN (5, 8, 9) THEN bd.quantity ELSE 0 END), 0)
                + COALESCE(SUM(ebd2.quantity_exchange), 0)
                - COALESCE(SUM(CASE WHEN rbeb.status = 1 THEN rbd2.quantity_return ELSE 0 END), 0)
            ), 0) AS SoLuong
        FROM
            DateRange dr
        LEFT JOIN dbo.Bill b
            ON CAST(b.update_date AS DATE) = dr.DateValue
            OR CAST(b.create_date AS DATE) = dr.DateValue
        LEFT JOIN dbo.bill_detail bd
            ON bd.id_bill = b.id
        LEFT JOIN dbo.return_bill_exchange_bill rbeb
            ON rbeb.id_bill = b.id
        LEFT JOIN dbo.exchange_bill_detail ebd2
            ON ebd2.id_exchang_bill = rbeb.id
        LEFT JOIN dbo.return_bill_detail rbd2
            ON rbd2.id_return_bill = rbeb.id
        GROUP BY
            dr.DateValue
        ORDER BY
            dr.DateValue ASC;
        
    """,nativeQuery = true)
    List<Object[]> findStatisticsByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query(value = """
    SELECT
        COALESCE(SUM(b.shipping_price), 0) + COALESCE(SUM(ex.exchange_and_return_fee), 0) AS total
    FROM
        bill b
    LEFT JOIN
        return_bill_exchange_bill ex ON ex.id_bill = b.id;
    
    """, nativeQuery = true)
    Long serviceFee();

    @Query(value = """
        select COALESCE(SUM(customer_refund - discounted_amount), 0)
        As total
        from return_bill_exchange_bill
    """,nativeQuery = true)
    Long returnFee();

    @Query(value = """
        SELECT COALESCE(SUM(customer_payment - discounted_amount), 0) 
        AS total
        FROM return_bill_exchange_bill;
    """, nativeQuery = true)
    Long exchangeFee();
}

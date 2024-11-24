package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface InvoiceStatusRepository extends JpaRepository<InvoiceStatus,Integer> {
    @Query("select invoi from InvoiceStatus invoi where invoi.bill.id = :idCheck ")
    List<InvoiceStatus> getALLInvoiceStatusByBill(@Param("idCheck") Integer idCheck);
    @Query(value = """
        select
        	status,
        	create_date,
            CASE
            WHEN LEFT(invoice_status.note, CHARINDEX(',', invoice_status.note) - 1) = N'Không có' THEN N'Không có'
            ELSE (select code_staff + '-' + full_name
                  from staff
                  where id = (SUBSTRING(invoice_status.note, 1, CHARINDEX(',', invoice_status.note) - 1)))
            END AS staff_info,
            SUBSTRING(invoice_status.note, CHARINDEX(',', invoice_status.note) + 1, LEN(invoice_status.note)) AS note_details
        	from invoice_status where id_bill = :idCheck
""",nativeQuery = true)
    List<Object[]> getHistoryByBill(@Param("idCheck") Integer idCheck);

    @Query(value = """
    select
        MIN(im.name_image),
    	p.name_product,
    	cl.name_color,
    	si.name_size,
    	bd.quantity,
    	bd.price_root,
    	bd.price
    from bill_detail bd
    left join product_detail pd on bd.id_product_detail = pd.id
    left join color cl on cl.id = pd.id_color
    left join size si on si.id = pd.id_size
    left join product p on p.id = pd.id_product
    left join image im on im.id_product = p.id
    where bd.id_bill = :idCheck
    GROUP BY
        p.name_product,
        cl.name_color,
        si.name_size,
        bd.quantity,
        bd.price_root,
        bd.price;
""",nativeQuery = true)
    List<Object[]> getAllProductBuyClient(@Param("idCheck") Integer idCheck);
    @Query(value = """
    select
    	b.total_amount,
    	b.shipping_price,
    	b.price_discount,
    	(b.total_amount-b.price_discount+b.shipping_price)
    from bill b where b.id = :idCheck
""",nativeQuery = true)
    List<Object[]> getBillClient(@Param("idCheck") Integer idCheck);
    @Query(value = """
	   SELECT
    b.code_bill,
    CASE
        WHEN b.address != N'Không có' THEN
            TRIM(SUBSTRING(b.address, 1, CHARINDEX(',', b.address) - 1))
        ELSE
            CASE
                WHEN b.id_customer IS NOT NULL THEN CAST(c.full_name AS NVARCHAR)
                ELSE N'khách lẻ'
            END
    END AS name_customer,
   \s
    -- Số điện thoại: 2 ký tự đầu, 2 ký tự cuối, còn lại là *
    CASE
        WHEN b.address != N'Không có' THEN
            TRIM(SUBSTRING(b.address, CHARINDEX(',', b.address) + 1, CHARINDEX(',', b.address, CHARINDEX(',', b.address) + 1) - CHARINDEX(',', b.address) - 1))
        ELSE
            CASE
                WHEN b.id_customer IS NOT NULL THEN\s
                    -- Ẩn phần giữa của số điện thoại, chỉ giữ lại 2 ký tự đầu và 2 ký tự cuối
                    LEFT(c.number_phone, 2) + REPLICATE('*', LEN(c.number_phone) - 4) + RIGHT(c.number_phone, 2)
                ELSE N'Không có'
            END
    END AS number_phone_customer,
   \s
    -- Email: 2 ký tự đầu, 2 ký tự cuối, còn lại là *
    CASE
        WHEN b.address != N'Không có' THEN
            TRIM(SUBSTRING(b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address) + 1) + 1, CHARINDEX(',', b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address) + 1) + 1) - CHARINDEX(',', b.address, CHARINDEX(',', b.address) + 1) - 1))
        ELSE
            CASE
                WHEN b.id_customer IS NOT NULL THEN\s
                    -- Ẩn phần giữa của email, chỉ giữ lại 2 ký tự đầu và 2 ký tự cuối
                    LEFT(c.email, 2) + REPLICATE('*', LEN(c.email) - 4) + RIGHT(c.email, 2)
                ELSE N'Không có'
            END
    END AS email_customer,
   \s
    CASE
        WHEN b.address != N'Không có' THEN
            TRIM(SUBSTRING(b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address, CHARINDEX(',', b.address) + 1) + 1) + 1) + 1) + 1) + 1, LEN(b.address)))
        ELSE
            CASE
                WHEN b.id_customer IS NOT NULL THEN CAST(TRIM(SUBSTRING(c.address, CHARINDEX(',', c.address, CHARINDEX(',', c.address, CHARINDEX(',', c.address) + 1) + 1) + 1, LEN(c.address))) AS NVARCHAR)
                ELSE N'Không có'
            END
    END AS add_ress,
   \s
    b.note
            FROM bill b
            LEFT JOIN customer c ON c.id = b.id_customer
            LEFT JOIN voucher v ON v.id = b.id_voucher
            WHERE b.id = :idCheck
""",nativeQuery = true)
    List<Object[]> getInformationBillStatusClient(@Param("idCheck") Integer idCheck);

    //xuat du lieu nhan vien da lam gi
    @Query(value = """
    select
        b.id,
        b.code_bill,
        inst.status,
        FORMAT(inst.create_date, 'HH:mm:ss dd-MM-yyyy') AS formatted_date
    from
        invoice_status inst
    left join bill b
        on b.id = inst.id_bill
    where
        SUBSTRING(inst.note, 1, CHARINDEX(',', inst.note) - 1) != 'Không có'
        and CAST(SUBSTRING(inst.note, 1, CHARINDEX(',', inst.note) - 1) AS INT) = :idStaff
        and inst.create_date BETWEEN :startDate AND :endDate
        AND CONVERT(TIME, inst.create_date) BETWEEN :startTime AND :endTime
    order by
        inst.create_date desc,
        b.code_bill desc,
        inst.status asc
""",nativeQuery = true)
    List<Object[]> getAllInvoiceStatusByStaff(
            @Param("idStaff") Integer idStaff,
            @Param("startDate")Date startDate,
            @Param("endDate") Date endDate,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime
            );


}

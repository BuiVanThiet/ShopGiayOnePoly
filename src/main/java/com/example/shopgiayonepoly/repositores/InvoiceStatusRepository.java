package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}

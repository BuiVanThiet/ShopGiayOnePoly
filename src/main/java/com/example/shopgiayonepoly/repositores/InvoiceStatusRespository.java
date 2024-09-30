package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ResponseBody
public interface InvoiceStatusRespository extends JpaRepository<InvoiceStatus,Integer> {
    @Query("select invoi from InvoiceStatus invoi where invoi.bill.id = :idCheck ")
    List<InvoiceStatus> getALLInvoiceStatusByBill(@Param("idCheck") Integer idCheck);
    @Query(value = """
        select
        	status,
        	create_date,
        	(select code_staff+'-'+full_name from staff where id = (SUBSTRING(invoice_status.note, 1, CHARINDEX(',', invoice_status.note) - 1))),
        	SUBSTRING(invoice_status.note, CHARINDEX(',', invoice_status.note) + 1, LEN(invoice_status.note))
        	from invoice_status where id_bill = :idCheck
""",nativeQuery = true)
    List<Object[]> getHistoryByBill(@Param("idCheck") Integer idCheck);
}

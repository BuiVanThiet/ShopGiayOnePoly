package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ResponseBody
public interface InvoiceStatusRespository extends JpaRepository<InvoiceStatus,Integer> {
    @Query("select invoi from InvoiceStatus invoi where invoi.bill.id = :idCheck")
    List<InvoiceStatus> getALLInvoiceStatusByBill(@Param("idCheck") Integer idCheck);
}

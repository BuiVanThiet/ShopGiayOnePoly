package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.ReturnBillExchangeBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnBillReponsetory extends JpaRepository<ReturnBillExchangeBill,Integer> {
    @Query("select rb from ReturnBillExchangeBill rb where rb.bill.id = :idBill")
    ReturnBillExchangeBill getReturnBillByIdBill(@Param("idBill") Integer idBill);
}

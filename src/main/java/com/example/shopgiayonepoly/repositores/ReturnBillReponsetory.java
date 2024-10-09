package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.ReturnBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnBillReponsetory extends JpaRepository<ReturnBill,Integer> {
    @Query("select rb from ReturnBill rb where rb.bill.id = :idBill")
    ReturnBill getReturnBillByIdBill(@Param("idBill") Integer idBill);
}

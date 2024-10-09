package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.ReturnBillDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnBillDetailReponsetory extends JpaRepository<ReturnBillDetail,Integer> {
    @Query("select rbd from ReturnBillDetail rbd where rbd.returnBill.id = :idReturnBill")
    Page<ReturnBillDetail> getReturnBillDetailByIdReturnBill(@Param("idReturnBill") Integer idReturnBill, Pageable pageable);
    @Query("select rbd from ReturnBillDetail rbd where rbd.returnBill.id = :idReturnBill")
    List<ReturnBillDetail> getReturnBillDetailByIdReturnBill(@Param("idReturnBill") Integer idReturnBill);
}



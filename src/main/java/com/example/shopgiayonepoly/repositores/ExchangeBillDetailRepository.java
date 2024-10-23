package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.ExchangeBillDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeBillDetailRepository extends JpaRepository<ExchangeBillDetail,Integer> {
    @Query("select ebd from ExchangeBillDetail ebd where ebd.exchangeBill.id = :idExchangeBill")
    Page<ExchangeBillDetail> getExchangeBillDetailByIdReturnBill(@Param("idExchangeBill") Integer idExchangeBill, Pageable pageable);
    @Query("select ebd from ExchangeBillDetail ebd where ebd.exchangeBill.id = :idExchangeBill")
    List<ExchangeBillDetail> getExchangeBillDetailByIdReturnBill(@Param("idExchangeBill") Integer idExchangeBill);
}

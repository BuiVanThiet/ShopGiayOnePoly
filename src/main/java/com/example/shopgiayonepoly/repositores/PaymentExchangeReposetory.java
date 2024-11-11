package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.PaymentExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentExchangeReposetory extends JpaRepository<PaymentExchange,Integer> {
    @Query("""
    select pay from PaymentExchange pay where pay.exchangeBilll.id = :idCheck 
""")
    List<PaymentExchange> getPaymentExchangeByIdBillExchange(@Param("idCheck") Integer idCheck);
}

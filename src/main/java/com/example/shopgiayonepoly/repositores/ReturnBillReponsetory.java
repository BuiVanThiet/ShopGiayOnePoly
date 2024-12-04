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
    @Query("""
            select
                           case
                               when (rb.customerRefund - rb.exchangeAndReturnFee + rb.discountedAmount) - rb.customerPayment < -1
                               then 'PhaiTraTien'
                               else 'KhongPhaiTraTien'
                           end
                       from ReturnBillExchangeBill rb 
                       where rb.id = :idCheck and rb.status = 0
                       """)
    String getStatusPaymentorNotPay(@Param("idCheck") Integer idCheck);
}

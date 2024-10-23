package com.example.shopgiayonepoly.entites;

import com.example.shopgiayonepoly.entites.baseEntity.Base;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "return_bill_exchange_bill")
@ToString
public class ReturnBillExchangeBill extends Base {
    @ManyToOne
    @JoinColumn(name = "id_bill")
    private Bill bill;
    @Column(name = "code_return_bill_exchange_bill")
    private String codeReturnBillExchangBill;
    @Column(name = "customer_refund")
    private BigDecimal customerRefund;
    @Column(name = "customer_payment")
    private BigDecimal customerPayment;
    @Column(name = "reason")
    private String reason;
}

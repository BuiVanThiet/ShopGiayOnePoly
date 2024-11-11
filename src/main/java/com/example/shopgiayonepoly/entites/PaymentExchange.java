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
@Table(name = "payment_exchange")
@ToString
public class PaymentExchange extends Base {
    @ManyToOne
    @JoinColumn(name = "id_exchange")
    private ReturnBillExchangeBill exchangeBilll;
    @ManyToOne
    @JoinColumn(name = "id_staff")
    private Staff staff;
    @Column(name = "cash")
    private BigDecimal cash;
    @Column(name = "cash_acount")
    private BigDecimal cashAcount;
    @Column(name = "pay_method")
    private Integer payMethod;
    @Column(name = "surplus_money")
    private BigDecimal surplusMoney;
}

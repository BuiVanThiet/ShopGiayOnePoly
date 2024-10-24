package com.example.shopgiayonepoly.entites;

import com.example.shopgiayonepoly.entites.baseEntity.Base;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exchange_bill_detail")
public class ExchangeBillDetail extends Base {
    @ManyToOne
    @JoinColumn(name = "id_exchang_bill")
    private ReturnBillExchangeBill exchangeBill;
    @ManyToOne
    @JoinColumn(name = "id_product_detail")
    private ProductDetail productDetail;
    @Column(name = "quantity_exchange")
    private Integer quantityExchange;
    @Column(name = "price_at_the_time_of_exchange")
    private BigDecimal priceAtTheTimeOfExchange;
    @Column(name = "total_exchange")
    private BigDecimal totalExchange;
    @Column(name = "price_root_at_time")
    private BigDecimal priceRootAtTime;
}

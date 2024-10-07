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
@Table(name = "return_bill_detail")
public class ReturnBillDetail extends Base {
    @ManyToOne
    @JoinColumn(name = "id_return_bill")
    private ReturnBill returnBill;
    @ManyToOne
    @JoinColumn(name = "id_product_detail")
    private ProductDetail productDetail;
    @Column(name = "quantity_return")
    private Integer quantityReturn;
    @Column(name = "price_buy")
    private BigDecimal priceBuy;
    @Column(name = "total_return")
    private BigDecimal totalReturn;
}

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
@Table(name = "bill_detail")
@ToString
public class BillDetail extends Base {
    @ManyToOne
    @JoinColumn(name = "id_bill")
    private Bill bill;
    @ManyToOne
    @JoinColumn(name = "id_product_detail")
    private ProductDetail productDetail;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "price_root")
    private BigDecimal priceRoot;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
}

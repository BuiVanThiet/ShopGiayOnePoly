package com.example.shopgiayonepoly.entites;

import com.example.shopgiayonepoly.entites.baseEntity.Base;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_detail")
@ToString
public class ProductDetail extends Base {
    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "id_color")
    private Color color;
    @ManyToOne
    @JoinColumn(name = "id_size")
    private Size size;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "import_price")
    private BigDecimal import_price;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "describe")
    private String describe;
    @Column(name = "high")
    private Double high;
    @Column(name = "width")
    private Double width;
    @Column(name = "wight")
    private Double wight;
    @Column(name = "lenght_product")
    private Double leng_product;
    @ManyToOne
    @JoinColumn(name = "id_sale_product")
    private SaleProduct saleProduct;
    @Transient
    private BigDecimal originalPrice;
}

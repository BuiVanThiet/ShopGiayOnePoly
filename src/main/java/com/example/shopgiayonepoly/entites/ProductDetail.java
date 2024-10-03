package com.example.shopgiayonepoly.entites;

import com.example.shopgiayonepoly.entites.baseEntity.Base;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_detail")
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
    private Float high;
    @Column(name = "width")
    private Float width;
    @Column(name = "wight")
    private Float wight;
    @Column(name = "lenght_product")
    private Float lenght_product;
    @ManyToOne
    @JoinColumn(name = "id_sale_product")
    private SaleProduct saleProduct;
}

package com.example.shopgiayonepoly.dto.response;

import com.example.shopgiayonepoly.entites.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponse {
    private Product product;
    private String Color;
    private String Size;
    private Integer status;
    private String describe;
    private BigDecimal price;
    private BigDecimal importPrice;
    private Double weight;
    private Integer idSaleProduct;
    private Long totalQuantity;

}

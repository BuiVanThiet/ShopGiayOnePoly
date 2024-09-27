package com.example.shopgiayonepoly.dto.response.bill;

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
public class ProductDetailSellResponse {
    private Product product;
    private Integer quentity;
    private BigDecimal price;
    private BigDecimal priceSale;
}

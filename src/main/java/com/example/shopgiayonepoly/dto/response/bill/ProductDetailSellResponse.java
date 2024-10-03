package com.example.shopgiayonepoly.dto.response.bill;

import com.example.shopgiayonepoly.entites.Color;
import com.example.shopgiayonepoly.entites.Product;
import com.example.shopgiayonepoly.entites.SaleProduct;
import com.example.shopgiayonepoly.entites.Size;
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
    private Integer id;
    private String nameProduct;
    private String nameColor;
    private String size;
    private Integer quantity;
    private BigDecimal price;
    private Integer status;
    private SaleProduct saleProduct;
}

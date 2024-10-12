package com.example.shopgiayonepoly.dto.request;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import com.example.shopgiayonepoly.entites.Product;
import com.example.shopgiayonepoly.entites.SaleProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDiscountRequest extends BaseDTO {
    private Integer id;
    private BigDecimal originalPrice;
    private BigDecimal newPrice;
    private Product product;
    private SaleProduct saleProduct;
}

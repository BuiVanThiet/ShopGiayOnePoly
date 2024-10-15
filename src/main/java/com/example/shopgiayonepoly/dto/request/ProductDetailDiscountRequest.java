package com.example.shopgiayonepoly.dto.request;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import com.example.shopgiayonepoly.entites.Product;
import com.example.shopgiayonepoly.entites.SaleProduct;
import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDetailDiscountRequest extends BaseDTO {
    private Integer id;
    private BigDecimal originalPrice;
    private BigDecimal newPrice;
    private Product product;
    private SaleProduct saleProduct;
}

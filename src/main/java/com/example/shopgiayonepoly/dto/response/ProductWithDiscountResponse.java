package com.example.shopgiayonepoly.dto.response;

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
public class ProductWithDiscountResponse extends BaseDTO {
    private Integer productId;
    private String productName;
    private Integer saleProductId;
    private String saleProductName;

}

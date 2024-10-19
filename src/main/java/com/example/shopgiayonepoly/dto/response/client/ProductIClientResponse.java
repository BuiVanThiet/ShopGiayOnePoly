package com.example.shopgiayonepoly.dto.response.client;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductIClientResponse{
    private Integer id;
    private String nameProduct;
    private String images;
    private BigDecimal priceProduct;
}

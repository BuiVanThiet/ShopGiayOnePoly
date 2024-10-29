package com.example.shopgiayonepoly.dto.response.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor

public class ProductDetailClientRespone {
    private Integer productDetailId;
    private Integer productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private String productDetailDescription;
    private String colorName;
    private String sizeName;
    private String productImage;

    public ProductDetailClientRespone(Integer productDetailId,
                                      Integer productId,
                                      String productName,
                                      BigDecimal price,
                                      Integer quantity,
                                      String productDetailDescription,
                                      String colorName,
                                      String sizeName,
                                      String productImage) {
        this.productDetailId = productDetailId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.productDetailDescription = productDetailDescription;
        this.colorName = colorName;
        this.sizeName = sizeName;
        this.productImage = productImage;
    }
    public ProductDetailClientRespone(Integer id, BigDecimal price, Integer quantity) {
        this.productDetailId = id;
        this.price = price;
        this.quantity = quantity;
    }
}


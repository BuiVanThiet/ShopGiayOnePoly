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
    private Long productId;
    private String productName;
    private String productCode;
    private String productDescription;
    private BigDecimal price;
    private BigDecimal importPrice;
    private Integer quantity;
    private String productDetailDescription;
    private String colorName;
    private String sizeName;
    private BigDecimal discountPercentage;
    private String productImage;

    public ProductDetailClientRespone(Integer productDetailId,
                                      Long productId,
                                      String productName,
                                      String productCode,
                                      String productDescription,
                                      BigDecimal price,
                                      BigDecimal importPrice,
                                      Integer quantity,
                                      String productDetailDescription,
                                      String colorName,
                                      String sizeName,
                                      BigDecimal discountPercentage,
                                      String productImage) {
        this.productDetailId = productDetailId;
        this.productId = productId;
        this.productName = productName;
        this.productCode = productCode;
        this.productDescription = productDescription;
        this.price = price;
        this.importPrice = importPrice;
        this.quantity = quantity;
        this.productDetailDescription = productDetailDescription;
        this.colorName = colorName;
        this.sizeName = sizeName;
        this.discountPercentage = discountPercentage;
        this.productImage = productImage;
    }

}


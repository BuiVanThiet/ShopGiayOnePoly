package com.example.shopgiayonepoly.dto.response.client;

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
    private BigDecimal priceDiscount;
    private Integer quantity;
    private String productDetailDescription;
    private String colorName;
    private String sizeName;
    private String productImage;
    private Integer idSaleProduct;
    private String materialName;
    private String manufacturerName;
    private String originName;

    public ProductDetailClientRespone(Integer productDetailId,
                                      Integer productId,
                                      String productName,
                                      BigDecimal price,
                                      BigDecimal priceDiscount,
                                      Integer quantity,
                                      String productDetailDescription,
                                      String colorName,
                                      String sizeName,
                                      Integer idSaleProduct) {
        this.productDetailId = productDetailId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.priceDiscount = priceDiscount;
        this.quantity = quantity;
        this.productDetailDescription = productDetailDescription;
        this.colorName = colorName;
        this.sizeName = sizeName;
        this.idSaleProduct = idSaleProduct;
    }

    public ProductDetailClientRespone(
            Integer productDetailId,
            Integer productId,
            String productName,
            BigDecimal price,
            BigDecimal priceDiscount,
            Integer quantity,
            String productDetailDescription,
            String colorName,
            String sizeName,
            String productImage,
            String materialName,
            String manufacturerName,
            String originName) {
        this.productDetailId = productDetailId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.priceDiscount = priceDiscount;
        this.quantity = quantity;
        this.productDetailDescription = productDetailDescription;
        this.colorName = colorName;
        this.sizeName = sizeName;
        this.productImage = productImage;
        this.materialName = materialName;
        this.manufacturerName = manufacturerName;
        this.originName = originName;

    }


    public ProductDetailClientRespone(Integer id, BigDecimal price, Integer quantity) {
        this.productDetailId = id;
        this.price = price;
        this.quantity = quantity;
    }
}


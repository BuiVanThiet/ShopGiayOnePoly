package com.example.shopgiayonepoly.dto.response.client;

import com.example.shopgiayonepoly.entites.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CartResponse {
    private Integer cartId;
    private Integer customerId;
    private Integer productDetailId;
    private String productName;
    private String colorName;
    private String sizeName;
    private Integer quantity;
    private BigDecimal originalPrice;
    private BigDecimal discountedPrice;
    private List<Image> imageName;
}

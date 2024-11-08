package com.example.shopgiayonepoly.dto.request;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductInfoDto extends BaseDTO  {
    private String productName;

    private String colorName;

    private String sizeName;

    private String originalPrice;

    private String promotionalPrice;

    private int totalQuantity;

    private List<String> imageUrls;

}

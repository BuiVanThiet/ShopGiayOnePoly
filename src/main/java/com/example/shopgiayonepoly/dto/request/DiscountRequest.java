package com.example.shopgiayonepoly.dto.request;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DiscountRequest {
    private List<Integer> productIds;
    private BigDecimal discountValue;
    private Integer discountType;
}

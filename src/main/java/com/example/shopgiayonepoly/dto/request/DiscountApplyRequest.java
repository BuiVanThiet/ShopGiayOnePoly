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
public class DiscountApplyRequest extends BaseDTO {
    private List<Integer> productIds;
    private BigDecimal discountValue;
    private Integer discountType;
    private Integer saleProductId;
}

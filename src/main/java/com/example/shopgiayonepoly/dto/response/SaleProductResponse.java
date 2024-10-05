package com.example.shopgiayonepoly.dto.response;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SaleProductResponse extends BaseDTO {
    private String codeSale;
    private String nameSale;
    private Integer discountType;
    private BigDecimal discountValue;
    private LocalDate startDate;
    private LocalDate endDate;
}

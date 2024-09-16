package com.example.shopgiayonepoly.dto.request;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoucherRequest extends BaseDTO {
    private String codeVoucher;
    private String nameVoucher;
    private Integer discountType;
    private BigDecimal priceReduced;
    private BigDecimal pricesApply;
    private BigDecimal pricesMax;
    private LocalDate startDate;
    private LocalDate endDate;
    private String describe;


}

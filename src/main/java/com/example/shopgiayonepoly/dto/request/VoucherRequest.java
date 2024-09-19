package com.example.shopgiayonepoly.dto.request;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank
    private String codeVoucher;
    @NotBlank
    private String nameVoucher;
    @NotNull
    private Integer discountType;
    @NotNull
    private BigDecimal priceReduced;
    @NotNull
    private BigDecimal pricesApply;
    @NotNull
    private BigDecimal pricesMax;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotBlank
    private String describe;
    @NotNull
    private Integer quantity;


}

package com.example.shopgiayonepoly.dto.request;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VoucherRequest extends BaseDTO {
    @NotBlank(message = "Mã phiếu giảm giá không được để trống!")
    private String codeVoucher;
    @NotBlank(message = "Tên phiếu giảm giá không được để trống!")
    private String nameVoucher;
    @NotNull(message = "Hãy chọn loại phiếu giảm giá!")
    private Integer discountType;
    private BigDecimal priceReduced;
    @NotNull(message = "Giảm tối thiểu không được giảm giá!")
    private BigDecimal pricesApply;
    @NotNull(message = "Giá trị giảm tối đa không được để trống!")
    private BigDecimal pricesMax;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
    @NotBlank(message = "Mô tả không được để trống!")
    private String describe;
    @NotNull(message = "Số lượng không được để trống")
    private Integer quantity;


}

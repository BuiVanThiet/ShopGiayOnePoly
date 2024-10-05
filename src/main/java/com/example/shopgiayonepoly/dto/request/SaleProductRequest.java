package com.example.shopgiayonepoly.dto.request;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import jakarta.persistence.Column;
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
public class SaleProductRequest extends BaseDTO {
    @NotBlank(message = "Mã phiếu giảm giá không được để trống!")
    private String codeSale;
    @NotBlank(message = "Tên phiếu giảm giá không được để trống!")
    private String nameSale;
    @NotNull(message = "Hãy chọn loại phiếu giảm giá!")
    private Integer discountType;
    private BigDecimal discountValue;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
}

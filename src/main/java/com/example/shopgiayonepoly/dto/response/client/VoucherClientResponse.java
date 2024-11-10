package com.example.shopgiayonepoly.dto.response.client;

import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VoucherClientResponse {
    private Integer id;
    private String nameVoucher;
    private String codeVoucher;
    private Integer voucherType;
    private BigDecimal priceReduced;

}

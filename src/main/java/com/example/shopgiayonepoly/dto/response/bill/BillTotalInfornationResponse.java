package com.example.shopgiayonepoly.dto.response;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillTotalInfornationResponse {

    private Integer billId;
    private BigDecimal totalAmount;
    private Integer voucherId;
    private String codeVoucher;
    private String nameVoucher;
    private BigDecimal discount;
    private BigDecimal finalAmount;
    private String note;


}

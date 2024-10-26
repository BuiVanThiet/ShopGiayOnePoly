package com.example.shopgiayonepoly.dto.response.bill;

import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InfomationReturnBillResponse {
    private String codeBill;
    private String nameCustomer;
    private BigDecimal discount;
    private BigDecimal discountRatioPercentage;
    private Integer quantityBuy;
    private BigDecimal totalReturn;
    private BigDecimal totalExchange;
    private String noteReturn;
    private BigDecimal exchangeAndReturnFee;
    private BigDecimal discountedAmount;
}

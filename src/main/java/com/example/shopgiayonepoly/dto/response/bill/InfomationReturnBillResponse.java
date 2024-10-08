package com.example.shopgiayonepoly.dto.response.bill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InfomationReturnBillResponse {
    private String codeBill;
    private String nameCustomer;
    private BigDecimal discount;
    private BigDecimal discountRatioPercentage;
    private Integer quantityBuy;
    private BigDecimal totalReturn;
}

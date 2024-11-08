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
    private Integer id;
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
    private Integer status;

    public InfomationReturnBillResponse(String codeBill, String nameCustomer, BigDecimal discount, BigDecimal discountRatioPercentage, Integer quantityBuy, BigDecimal totalReturn, BigDecimal totalExchange, String noteReturn, BigDecimal exchangeAndReturnFee, BigDecimal discountedAmount) {
        this.codeBill = codeBill;
        this.nameCustomer = nameCustomer;
        this.discount = discount;
        this.discountRatioPercentage = discountRatioPercentage;
        this.quantityBuy = quantityBuy;
        this.totalReturn = totalReturn;
        this.totalExchange = totalExchange;
        this.noteReturn = noteReturn;
        this.exchangeAndReturnFee = exchangeAndReturnFee;
        this.discountedAmount = discountedAmount;
    }
}

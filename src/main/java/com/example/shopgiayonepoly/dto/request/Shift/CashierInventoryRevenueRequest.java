package com.example.shopgiayonepoly.dto.request.Shift;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CashierInventoryRevenueRequest {
    private Integer idStaff;
    private BigDecimal totalMoneyBill;
    private BigDecimal totalMoneyReturnBill;
    private BigDecimal totalMoneyExchangeBill;
}

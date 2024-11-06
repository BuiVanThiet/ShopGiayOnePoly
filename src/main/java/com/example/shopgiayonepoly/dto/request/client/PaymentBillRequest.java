package com.example.shopgiayonepoly.dto.request.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentBillRequest {
    private String addressShip;
    private BigDecimal priceVoucher;
    private BigDecimal shippingPrice;
    private String noteBill;
    private BigDecimal totalAmountBill;


}

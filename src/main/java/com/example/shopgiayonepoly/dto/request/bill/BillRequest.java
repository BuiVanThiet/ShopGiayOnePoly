package com.example.shopgiayonepoly.dto.request.bill;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillRequest extends BaseDTO {
    private String codeBill;
    private Integer client;
    private Integer staff;
    private String addRess;
    private Integer voucher;
    private BigDecimal shippingPrice;
    private BigDecimal cash;
    private BigDecimal acountMoney;
    private String note;
    private BigDecimal totalAmount;
    private Integer paymentMethod;
    private Integer billType;
    private Integer paymentStatus;
}

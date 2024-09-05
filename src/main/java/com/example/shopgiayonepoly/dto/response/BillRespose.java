package com.example.shopgiayonepoly.dto.response;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import com.example.shopgiayonepoly.entites.Client;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.entites.Voucher;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillRespose extends BaseDTO {
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

package com.example.shopgiayonepoly.dto.response.bill;

import ch.qos.logback.core.net.server.Client;
import com.example.shopgiayonepoly.dto.base.BaseDTO;
import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.entites.Staff;
import com.example.shopgiayonepoly.entites.Voucher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillResponseManage extends BaseDTO {
    private String codeBill;
    private Customer customer;
    private Staff staff;
    private String addRess;
    private Voucher voucher;
    private BigDecimal shippingPrice;
    private BigDecimal cash;
    private BigDecimal acountMoney;
    private String note;
    private BigDecimal finalAmount;
    private Integer paymentMethod;
    private Integer billType;
    private Integer paymentStatus;
    private BigDecimal surplusMoney;

    public BillResponseManage(Integer id, String codeBill, Customer customer, Staff staff, String addRess, Voucher voucher,
                              BigDecimal shippingPrice, BigDecimal cash, BigDecimal acountMoney, String note,
                              BigDecimal finalAmount, Integer paymentMethod, Integer billType, Integer paymentStatus,
                              BigDecimal surplusMoney, Date createDate, Date updateDate, Integer status) {
        super(id, createDate, updateDate, status);
        this.codeBill = codeBill;
        this.customer = customer;
        this.staff = staff;
        this.addRess = addRess;
        this.voucher = voucher;
        this.shippingPrice = shippingPrice;
        this.cash = cash;
        this.acountMoney = acountMoney;
        this.note = note;
        this.finalAmount = finalAmount;
        this.paymentMethod = paymentMethod;
        this.billType = billType;
        this.paymentStatus = paymentStatus;
        this.surplusMoney = surplusMoney;
    }
}

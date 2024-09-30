package com.example.shopgiayonepoly.dto.response.bill;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import com.example.shopgiayonepoly.entites.Voucher;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InformationBillByIdBillResponse extends BaseDTO {
    private String codeBill;
    private Integer billMethod;
    private BigDecimal shipPrice;
    private BigDecimal totalPriceProduct;
    private Voucher voucher;
    private BigDecimal maximumReduction;
    private Integer paymentStatus;
    private String note;

    public InformationBillByIdBillResponse(Integer id, Date createDate, Date updateDate, @NotNull(message = "Mời bạn chọn trạng thái!") Integer status, String codeBill, Integer billMethod, BigDecimal shipPrice, BigDecimal totalPriceProduct, Voucher voucher, BigDecimal maximumReduction, Integer paymentStatus, String note) {
        super(id, createDate, updateDate, status);
        this.codeBill = codeBill;
        this.billMethod = billMethod;
        this.shipPrice = shipPrice;
        this.totalPriceProduct = totalPriceProduct;
        this.voucher = voucher;
        this.maximumReduction = maximumReduction;
        this.paymentStatus = paymentStatus;
        this.note = note;
    }
}

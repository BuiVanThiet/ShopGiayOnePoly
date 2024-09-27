package com.example.shopgiayonepoly.dto.response.bill;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import com.example.shopgiayonepoly.entites.Customer;
import jakarta.validation.constraints.NotNull;
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
public class InformationManageBillResponse extends BaseDTO {
    private String codeBill;
    private Customer customer;
    private BigDecimal total;
    private Integer billType;

    public InformationManageBillResponse(Integer id, Date createDate, Date updateDate, @NotNull(message = "Mời bạn chọn trạng thái!") Integer status, String codeBill, Customer customer, BigDecimal total, Integer billType) {
        super(id, createDate, updateDate, status);
        this.codeBill = codeBill;
        this.customer = customer;
        this.total = total;
        this.billType = billType;
    }
}

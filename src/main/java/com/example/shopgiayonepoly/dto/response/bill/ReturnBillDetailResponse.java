package com.example.shopgiayonepoly.dto.response.bill;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import com.example.shopgiayonepoly.entites.ProductDetail;
import com.example.shopgiayonepoly.entites.ReturnBill;
import com.example.shopgiayonepoly.entites.baseEntity.Base;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReturnBillDetailResponse extends BaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private ReturnBill returnBill;
    private ProductDetail productDetail;
    private Integer quantityReturn;
    private BigDecimal priceBuy;
    private BigDecimal totalReturn;
}

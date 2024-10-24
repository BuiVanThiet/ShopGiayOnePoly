package com.example.shopgiayonepoly.dto.response.bill;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import com.example.shopgiayonepoly.entites.ProductDetail;
import com.example.shopgiayonepoly.entites.ReturnBillExchangeBill;
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
    private ReturnBillExchangeBill returnBill;
    private ProductDetail productDetail;
    private Integer quantityReturn;
    private BigDecimal priceBuy;
    private BigDecimal priceDiscount;
    private BigDecimal totalReturn;
}

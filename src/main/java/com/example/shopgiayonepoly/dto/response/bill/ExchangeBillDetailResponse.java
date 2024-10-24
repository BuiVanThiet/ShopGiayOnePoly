package com.example.shopgiayonepoly.dto.response.bill;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import com.example.shopgiayonepoly.entites.ProductDetail;
import com.example.shopgiayonepoly.entites.ReturnBillExchangeBill;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ExchangeBillDetailResponse extends BaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private ProductDetail productDetail;
    private ReturnBillExchangeBill exchangeBill;
    private Integer quantityExchange;
    private BigDecimal priceAtTheTimeOfExchange;
    private BigDecimal totalExchange;
    private BigDecimal priceRootAtTime;
}

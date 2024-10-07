package com.example.shopgiayonepoly.dto.request.bill;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReturnBillDetailRequest {
    private Integer idProductDetail;
    private Integer quantityReturn;
    private BigDecimal priceBuy;
}

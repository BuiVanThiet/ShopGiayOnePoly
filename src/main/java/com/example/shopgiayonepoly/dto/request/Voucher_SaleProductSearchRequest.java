package com.example.shopgiayonepoly.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Voucher_SaleProductSearchRequest {
    private Integer discountTypeCheck;
    private String nameCheck;
    private Integer statusCheck;
}

package com.example.shopgiayonepoly.dto.request.bill;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BillDetailAjax {
    private Integer id;
    private Integer quantity;
    private String method;
}

package com.example.shopgiayonepoly.dto.request.bill;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductUpdateRequest {
    private Integer id;
    private Integer quantity;
}

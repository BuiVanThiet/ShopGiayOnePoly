package com.example.shopgiayonepoly.dto.response.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CartResponse {
    private Integer id;
    private Integer customerID;
    private Integer productDetailID;
    private Integer quantity;
}

package com.example.shopgiayonepoly.dto.response.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressShipReponse {
    private Integer id; // Địa chỉ gốc
    private String originalAddress; // Địa chỉ gốc
    private String shortAddress;    // Địa chỉ rút gọn
}

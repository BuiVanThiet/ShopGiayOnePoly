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
    private String originalAddress; // Địa chỉ gốc
    private String shortAddress;    // Địa chỉ rút gọn
}

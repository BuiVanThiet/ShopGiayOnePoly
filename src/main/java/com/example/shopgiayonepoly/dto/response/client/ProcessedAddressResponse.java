package com.example.shopgiayonepoly.dto.response.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedAddressResponse {
    private String idWard;
    private String idDistrict;
    private String idProvince;
    private String originalAddress;
    private String fullAddress;

}

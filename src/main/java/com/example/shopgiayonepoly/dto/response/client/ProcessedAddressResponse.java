package com.example.shopgiayonepoly.dto.response.client;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProcessedAddressResponse {
    private String idWard;
    private String idDistrict;
    private String idProvince;
    private String originalAddress;
    private String fullAddress;

}

package com.example.shopgiayonepoly.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientBillInformationResponse {
    private String name;
    private String numberPhone;
    private String city;
    private String district;
    private String commune;
    private String addressDetail;
}

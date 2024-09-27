package com.example.shopgiayonepoly.dto.response.bill;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClientBillInformationResponse {
    private String name;
    private String numberPhone;
    private String email;
    private String city;
    private String district;
    private String commune;
    private String addressDetail;

    public ClientBillInformationResponse(String name, String numberPhone, String city, String district, String commune, String addressDetail) {
        this.name = name;
        this.numberPhone = numberPhone;
        this.city = city;
        this.district = district;
        this.commune = commune;
        this.addressDetail = addressDetail;
    }
}

package com.example.shopgiayonepoly.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClientBillInformationResponse {
    private String name;
    private String numberPhone;
    private String city;
    private String district;
    private String commune;
    private String addressDetail;
}

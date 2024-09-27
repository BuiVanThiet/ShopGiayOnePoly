package com.example.shopgiayonepoly.dto.response.bill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InfomationCustomerBillResponse {
    private String fullName;
    private String numberPhone;
    private String email;

    private String addRessDetail;
}

package com.example.shopgiayonepoly.dto.request.client;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddressForCustomerRequest {
    private String phoneNumber;
    private String nameCustomer;
    private String emailCustomer;
    private String addressCustomer;

}

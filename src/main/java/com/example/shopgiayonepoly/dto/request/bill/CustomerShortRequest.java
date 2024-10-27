package com.example.shopgiayonepoly.dto.request.bill;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerShortRequest extends BaseDTO {
    private String nameCustomer;
    private String numberPhone;
    private String email;
    private String province;
    private String district;
    private String ward;
    private String addResDetail;
}

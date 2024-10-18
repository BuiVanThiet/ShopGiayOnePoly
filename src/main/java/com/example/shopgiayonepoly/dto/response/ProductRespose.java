package com.example.shopgiayonepoly.dto.response;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRespose extends BaseDTO {
    private Integer id;
    private String nameProduct;
    private String image;

}

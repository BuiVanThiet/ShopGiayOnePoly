package com.example.shopgiayonepoly.dto.request;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequet  {

        private int id;
        private String codeProduct;
        private String nameProduct;
        private String material;
        private String manufacturer;
        private String origin;
        private String sole;
        private String describe;
        private Integer status;




}

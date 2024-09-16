package com.example.shopgiayonepoly.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDetailCheckRequest {
    private String nameProduct;
    private Integer idColor;
    private Integer idSize;
    private Integer idMaterial;
    private Integer idManufacturer;
    private Integer idOrigin;
//    private List<Integer> idCategories;
}

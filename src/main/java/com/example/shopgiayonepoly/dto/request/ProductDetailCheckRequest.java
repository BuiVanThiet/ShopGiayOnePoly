package com.example.shopgiayonepoly.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailCheckRequest {
    private String nameProduct;
    private Integer idColor;
    private Integer idSize;
    private Integer idMaterial;
    private Integer idManufacturer;
    private Integer idOrigin;
    private List<Integer> idCategories;
}

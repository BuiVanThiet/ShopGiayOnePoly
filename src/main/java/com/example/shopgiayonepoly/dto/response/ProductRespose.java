package com.example.shopgiayonepoly.dto.response;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import com.example.shopgiayonepoly.entites.Manufacturer;
import com.example.shopgiayonepoly.entites.Material;
import com.example.shopgiayonepoly.entites.Origin;
import com.example.shopgiayonepoly.entites.Sole;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRespose {
    private Integer id;
    private String codeProduct;
    private String nameProduct;
    private Material material;
    private Manufacturer manufacturer;
    private Origin origin;
    private Sole sole;
    private String describe;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    private Integer status;
    private String image;

}

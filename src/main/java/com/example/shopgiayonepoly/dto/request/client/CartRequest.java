package com.example.shopgiayonepoly.dto.request.client;

import com.example.shopgiayonepoly.entites.Customer;
import com.example.shopgiayonepoly.entites.ProductDetail;
import com.example.shopgiayonepoly.entites.baseEntity.Base;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {
    private Customer customer;
    private ProductDetail productDetail;
    private Integer quantity;

}

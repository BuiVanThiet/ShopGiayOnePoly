package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.dto.response.client.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartService {
    CartResponse findByCustomerIDAndProductDetail(Integer customerID, Integer productDetailID);
}

package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.dto.response.client.ProductDetailClientRespone;
import com.example.shopgiayonepoly.dto.response.client.ProductIClientResponse;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientService {
   public List<ProductIClientResponse> getAllProduct();
   public List<ProductDetailClientRespone> findProductDetailByProductId(@Param("productId") Integer productId);
   public List<ProductIClientResponse> GetTop12ProductWithPriceHighest();
   public List<ProductIClientResponse> GetTop12ProductWithPriceLowest();
}

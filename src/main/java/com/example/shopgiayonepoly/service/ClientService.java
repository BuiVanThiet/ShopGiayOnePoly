package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.dto.response.client.*;
import com.example.shopgiayonepoly.entites.Cart;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ClientService {
    public List<ProductIClientResponse> getAllProduct();

    public List<ProductDetailClientRespone> findProductDetailByProductId(@Param("productId") Integer productId);

    public List<ProductIClientResponse> GetTop12ProductWithPriceHighest();

    public List<ProductIClientResponse> GetTop12ProductWithPriceLowest();

    public List<ColorClientResponse> findDistinctColorsByProductId(@Param("productId") Integer productId);

    public List<SizeClientResponse> findDistinctSizesByProductId(@Param("productId") Integer productId);

    ProductDetailClientRespone findByProductDetailColorAndSizeAndProductId(
            @Param("colorId") Integer colorId,
            @Param("sizeId") Integer sizeId,
            @Param("productId") Integer productId);

    BigDecimal findDiscountedPriceByProductDetailId(@Param("productDetailId") Integer productDetailId);

    VoucherClientResponse findVoucherApplyByID(Integer id);

    List<Cart> findListCartByIdCustomer(@Param("idCustomer") Integer idCustomer);

    void deleteCartByCustomerIdAndProductDetailId(Integer customerId, Integer productDetailId);
}

package com.example.shopgiayonepoly.service;

import com.example.shopgiayonepoly.dto.response.client.*;
import com.example.shopgiayonepoly.entites.Cart;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartService {
    Cart findByCustomerIDAndProductDetail(Integer customerID, Integer productDetailID);

    List<Cart> getCartItemsForCustomer(@Param("customerId") Integer customerId);

    void deleteCartByCustomerID(Integer customerId);

    public void updateCartItem(Cart cartItem);

    public void deleteCartItem(Integer cartId);
}

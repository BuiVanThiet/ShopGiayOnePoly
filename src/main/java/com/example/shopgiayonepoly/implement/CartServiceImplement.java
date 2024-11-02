package com.example.shopgiayonepoly.implement;

import com.example.shopgiayonepoly.dto.response.client.CartItemResponse;
import com.example.shopgiayonepoly.dto.response.client.CartResponse;
import com.example.shopgiayonepoly.entites.Cart;
import com.example.shopgiayonepoly.repositores.CartRepository;
import com.example.shopgiayonepoly.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImplement implements CartService {
    @Autowired
    CartRepository cartRepository;

    @Override
    public Cart findByCustomerIDAndProductDetail(Integer customerID, Integer productDetailID) {
        return cartRepository.findByCustomerIDAndProductDetail(customerID, productDetailID);
    }

    @Override
    public List<Cart> getCartItemsForCustomer(Integer customerId) {
        return cartRepository.getCartItemsForCustomer(customerId);
    }
}

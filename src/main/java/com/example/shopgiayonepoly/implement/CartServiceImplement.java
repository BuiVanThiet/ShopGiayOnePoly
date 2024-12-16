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

    @Override
    public void deleteCartByCustomerID(Integer customerId) {
        cartRepository.deleteCartByCustomerID(customerId);
    }

    @Override
    public void updateCartItem(Cart cartItem) {
        // Kiểm tra nếu đối tượng cartItem hợp lệ
        if (cartItem != null) {
            cartRepository.save(cartItem); // Cập nhật lại đối tượng giỏ hàng trong cơ sở dữ liệu
        }
    }

    @Override
    public void deleteCartItem(Integer cartId) {
        cartRepository.deleteById(cartId);
    }
}

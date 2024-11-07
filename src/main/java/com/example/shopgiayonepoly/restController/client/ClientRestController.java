package com.example.shopgiayonepoly.restController.client;

import com.example.shopgiayonepoly.dto.response.ClientLoginResponse;
import com.example.shopgiayonepoly.dto.response.client.CartItemResponse;
import com.example.shopgiayonepoly.dto.response.client.ProductDetailClientRespone;
import com.example.shopgiayonepoly.dto.response.client.ProductIClientResponse;
import com.example.shopgiayonepoly.entites.ProductDetail;
import com.example.shopgiayonepoly.implement.CustomerRegisterImplement;
import com.example.shopgiayonepoly.repositores.ClientSecurityResponsetory;
import com.example.shopgiayonepoly.repositores.CustomerRegisterRepository;
import com.example.shopgiayonepoly.repositores.ProductDetailRepository;
import com.example.shopgiayonepoly.service.CartService;
import com.example.shopgiayonepoly.service.ClientService;
import com.example.shopgiayonepoly.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api-client")
public class ClientRestController {
    @Autowired
    ClientSecurityResponsetory clientLoginResponse;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CustomerRegisterImplement customerRegisterImplement;
    @Autowired
    CustomerRegisterRepository customerRegisterRepository;
    @Autowired
    CustomerService customerService;
    @Autowired
    ClientService clientService;
    @Autowired
    ProductDetailRepository productDetailRepository;
    @Autowired
    CartService cartService;

    @GetMapping("/products/top12-highest")
    public List<ProductIClientResponse> getTop12ProductHighest() {
        return clientService.GetTop12ProductWithPriceHighest();
    }

    @GetMapping("/products/top12-lowest")
    public List<ProductIClientResponse> getTop12ProducLowest() {
        return clientService.GetTop12ProductWithPriceLowest();
    }

    @GetMapping("/products/product-detail")
    public ProductDetailClientRespone getProductDetail(
            @RequestParam Integer productId,
            @RequestParam Integer colorId,
            @RequestParam Integer sizeId) {
        ProductDetailClientRespone productDetail = clientService.findByProductDetailColorAndSizeAndProductId(colorId, sizeId, productId);
        if (productDetail != null) {
            return productDetail;
        }
        return null;
    }

//    @GetMapping("/cart-items")
//    public ResponseEntity<List<CartItemResponse>> getCartItems(HttpSession session) {
//        List<CartItemResponse> cartItems = new ArrayList<>();
//        // Kiểm tra nếu người dùng đã đăng nhập
//        ClientLoginResponse clientLogin = (ClientLoginResponse) session.getAttribute("clientLogin");
//        if (clientLogin != null) {
//            Integer customerId = clientLogin.getId();
//            cartItems = cartService.getCartItemsForCustomer(customerId);
//        } else {
//            // Lấy giỏ hàng từ session nếu chưa đăng nhập
//            Map<Integer, Integer> sessionCart = (Map<Integer, Integer>) session.getAttribute("sessionCart");
//            if (sessionCart != null) {
//                for (Map.Entry<Integer, Integer> entry : sessionCart.entrySet()) {
//                    Integer productDetailId = entry.getKey();
//                    Integer quantity = entry.getValue();
//                    ProductDetail productDetail = productDetailRepository.findById(productDetailId).orElse(null);
//
//                    if (productDetail != null) {
//                        cartItems.add(new CartItemResponse(productDetail, quantity));
//                    }
//                }
//            }
//        }
//        return ResponseEntity.ok(cartItems);
//    }
}

//package com.example.shopgiayonepoly.baseMethod;
//
//import com.example.shopgiayonepoly.dto.response.client.CartResponse;
//import com.example.shopgiayonepoly.entites.Cart;
//import com.example.shopgiayonepoly.entites.ProductDetail;
//import com.example.shopgiayonepoly.service.BillService;
//import com.example.shopgiayonepoly.service.ClientService;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public abstract class BaseClient {
//    @Autowired
//    protected ClientService clientService;
//
//    protected List<CartResponse> getCartResponsesForCustomer(Integer customerId) {
//        List<Cart> cartItems = clientService.findListCartByIdCustomer(customerId);
//
//        return cartItems.stream().map(cartItem -> {
//            ProductDetail productDetail = cartItem.getProductDetail();
//            BigDecimal discountedPrice = clientService.findDiscountedPriceByProductDetailId(productDetail.getId());
//            return new CartResponse(
//                    cartItem.getId(), // ID của mục giỏ hàng
//                    cartItem.getCustomer().getId(), // ID khách hàng
//                    productDetail.getId(), // ID sản phẩm chi tiết
//                    productDetail.getProduct().getNameProduct(), // Tên sản phẩm
//                    cartItem.getQuantity(), // Số lượng
//                    productDetail.getPrice(), // Giá gốc
//                    discountedPrice // Giá sau giảm
//            );
//        }).collect(Collectors.toList());
//    }
//}

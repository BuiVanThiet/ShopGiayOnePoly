package com.example.shopgiayonepoly.dto.response.client;

import com.example.shopgiayonepoly.entites.ProductDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CartItemResponse {
    private Integer productDetailId;
    private String productName;
    private String color;
    private String size;
    private String imageName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;

    public CartItemResponse(ProductDetail productDetail, Integer quantity) {
        this.productDetailId = productDetail.getId();
        this.productName = productDetail.getProduct().getNameProduct();
        this.color = productDetail.getColor().getNameColor();
        this.size = productDetail.getSize().getCodeSize();

        // Lấy tên hình ảnh từ sản phẩm, cần kiểm tra danh sách không rỗng
        if (!productDetail.getProduct().getImages().isEmpty()) {
            this.imageName = productDetail.getProduct().getImages().get(0).getNameImage();
        } else {
            this.imageName = null;
        }

        this.price = productDetail.getPrice();
        this.quantity = quantity;
        this.totalPrice = price.multiply(BigDecimal.valueOf(quantity));
    }

    // Constructor nhận vào từng trường
    public CartItemResponse(Integer productDetailId, String productName, String color, String size, String imageName,
                            BigDecimal price, Integer quantity, BigDecimal totalPrice) {
        this.productDetailId = productDetailId;
        this.productName = productName;
        this.color = color;
        this.size = size;
        this.imageName = imageName; // Đã sửa lại kiểu dữ liệu để phù hợp
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}

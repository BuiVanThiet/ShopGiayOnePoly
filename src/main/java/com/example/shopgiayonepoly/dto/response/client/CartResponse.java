package com.example.shopgiayonepoly.dto.response.client;

import com.example.shopgiayonepoly.entites.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private Integer cartId;
    private Integer customerId;
    private Integer productDetailId;
    private String productName;
    private String colorName;
    private String sizeName;
    private Integer quantity;
    private BigDecimal originalPrice;
    private BigDecimal discountedPrice;
    private List<Image> imageName;

    // Thêm phương thức validate số lượng với số lượng tồn kho
    public boolean validateQuantity(Integer availableStock) {
        if (this.quantity > availableStock) {
            return false; // Số lượng yêu cầu vượt quá số lượng tồn kho
        }
        return true; // Số lượng hợp lệ
    }
}

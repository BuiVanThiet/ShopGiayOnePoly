package com.example.shopgiayonepoly.dto.response.bill;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import com.example.shopgiayonepoly.entites.Color;
import com.example.shopgiayonepoly.entites.Product;
import com.example.shopgiayonepoly.entites.SaleProduct;
import com.example.shopgiayonepoly.entites.Size;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailSellResponse extends BaseDTO {
    private Product product;
    private Color color;
    private Size size;
    private BigDecimal price;
    private BigDecimal import_price;
    private Integer quantity;
    private String describe;
    private Double high;
    private Double width;
    private Double wight;
    private Double leng_product;
    private SaleProduct saleProduct;

    public ProductDetailSellResponse(Integer id, Date createDate, Date updateDate, @NotNull(message = "Mời bạn chọn trạng thái!") Integer status, Product product, Color color, Size size, BigDecimal price, BigDecimal import_price, Integer quantity, String describe, Double high, Double width, Double wight, Double leng_product, SaleProduct saleProduct) {
        super(id, createDate, updateDate, status);
        this.product = product;
        this.color = color;
        this.size = size;
        this.price = price;
        this.import_price = import_price;
        this.quantity = quantity;
        this.describe = describe;
        this.high = high;
        this.width = width;
        this.wight = wight;
        this.leng_product = leng_product;
        this.saleProduct = saleProduct;
    }
}

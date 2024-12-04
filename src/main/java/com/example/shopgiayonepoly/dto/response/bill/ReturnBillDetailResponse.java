package com.example.shopgiayonepoly.dto.response.bill;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import com.example.shopgiayonepoly.entites.ProductDetail;
import com.example.shopgiayonepoly.entites.ReturnBillExchangeBill;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReturnBillDetailResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private ReturnBillExchangeBill returnBill;
    private ProductDetail productDetail;
    private Integer quantityReturn;
    private BigDecimal priceBuy;
    private BigDecimal priceDiscount;
    private BigDecimal totalReturn;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer status;
    private Integer quantityInStock;
    public ReturnBillDetailResponse(Integer id, ReturnBillExchangeBill returnBill, ProductDetail productDetail, Integer quantityReturn, BigDecimal priceBuy, BigDecimal priceDiscount, BigDecimal totalReturn, LocalDateTime createDate, LocalDateTime updateDate, Integer status) {
        this.id = id;
        this.returnBill = returnBill;
        this.productDetail = productDetail;
        this.quantityReturn = quantityReturn;
        this.priceBuy = priceBuy;
        this.priceDiscount = priceDiscount;
        this.totalReturn = totalReturn;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.status = status;
    }
}

package com.example.shopgiayonepoly.entites;

import com.example.shopgiayonepoly.entites.baseEntity.Base;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "voucher")
public class Voucher extends Base {
    @Column(name = "code_voucher")
    private String codeVoucher;
    @Column(name = "name_voucher")
    private String nameVoucher;
    @Column(name = "discount_type")
    private String discountType;
    @Column(name = "price_reduced")
    private BigDecimal priceReduced;
    @Column(name = "prices_apply")
    private BigDecimal pricesApply;
    @Column(name = "prices_max")
    private BigDecimal pricesMax;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "describe")
    private String describe;
}

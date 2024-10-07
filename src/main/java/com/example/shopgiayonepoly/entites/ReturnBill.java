package com.example.shopgiayonepoly.entites;

import com.example.shopgiayonepoly.entites.baseEntity.Base;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "return_bill")
public class ReturnBill extends Base {
    @ManyToOne
    @JoinColumn(name = "id_bill")
    private Bill bill;
    @Column(name = "code_return_bill")
    private String codeReturnBill;
    @Column(name = "total_return")
    private BigDecimal totalReturn;
    @Column(name = "reason")
    private String reason;
}

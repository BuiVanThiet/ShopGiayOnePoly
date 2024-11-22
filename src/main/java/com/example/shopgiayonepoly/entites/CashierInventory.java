package com.example.shopgiayonepoly.entites;

import com.example.shopgiayonepoly.entites.baseEntity.Base;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cashier_inventory")
public class CashierInventory extends Base {
    @ManyToOne
    @JoinColumn(name = "id_staff")
    private Staff staff;
    @Column(name = "time_start_root")
    private LocalTime timeStartRoot;
    @Column(name = "time_end_root")
    private LocalTime timeEndRoot;
    @Column(name = "totalMoneyBill")
    private BigDecimal totalMoneyBill;
    @Column(name = "totalMoneyReturnBill")
    private BigDecimal totalMoneyReturnBill;
    @Column(name = "totalMoneyExchangeBill")
    private BigDecimal totalMoneyExchangeBill;
}

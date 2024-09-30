package com.example.shopgiayonepoly.entites;

import com.example.shopgiayonepoly.entites.baseEntity.Base;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invoice_status")
public class InvoiceStatus extends Base {
    @ManyToOne
    @JoinColumn(name = "id_bill")
    private Bill bill;
    @Column(name = "note")
    private String note;
}

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
@Table(name = "address_ship")
public class AddressShip extends Base {
    @ManyToOne
    @JoinColumn(name = "id_customer")
    private Customer customer;
    @Column(name = "specific_address")
    private String specificAddress;
}

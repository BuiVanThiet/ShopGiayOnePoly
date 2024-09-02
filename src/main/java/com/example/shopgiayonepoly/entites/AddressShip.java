package com.example.shopgiayonepoly.entites;

import com.example.shopgiayonepoly.entites.baseEntity.Base;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "address_ship")
public class AddressShip extends Base {
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "number_phone")
    private String numberPhone;
    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client client;
    @Column(name = "province")
    private String province;
    @Column(name = "district")
    private String district;
    @Column(name = "commune")
    private String commune;
    @Column(name = "specific_address")
    private String specificAddress;
}

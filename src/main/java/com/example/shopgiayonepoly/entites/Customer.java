package com.example.shopgiayonepoly.entites;

import com.example.shopgiayonepoly.entites.baseEntity.Base;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")
@ToString
public class Customer extends Base {
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "number_phone")
    private String numberPhone;
    @Column(name = "birth_day")
    private LocalDate birthDay;
    @Column(name = "image")
    private String image;
    @Column(name = "email")
    private String email;
    @Column(name = "acount")
    private String acount;
    @Column(name = "password")
    private String password;
    @Column(name = "gender")
    private Integer gender;
    @Column(name = "address")
    private String addRess;
}

package com.example.shopgiayonepoly.entites;

import com.example.shopgiayonepoly.entites.baseEntity.Base;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "staff")
@ToString
public class Staff extends Base {
    @Column(name = "code_staff")
    private String codeStaff;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "address")
    private String address;
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
    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;
    
}

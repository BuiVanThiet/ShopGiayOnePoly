package com.example.shopgiayonepoly.dto.response;

import com.example.shopgiayonepoly.entites.baseEntity.Base;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class loginReponse extends Base {
    private String fullName;
    private String numberPhone;
    private LocalDate birthDay;
    private String image;
    private String email;
    private String acount;
    private String password;
    private Integer gender;
    private String addRess;
    private String role;
}

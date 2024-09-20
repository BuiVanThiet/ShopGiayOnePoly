package com.example.shopgiayonepoly.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String fullName;
    private String email;
    private String numberPhone;
    private String address;
    private String acount;
    private String password;
    private String confirmPassword;
    private Integer gender;
}

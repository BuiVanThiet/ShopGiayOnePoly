package com.example.shopgiayonepoly.dto.response;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClientLoginResponse extends BaseDTO {
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

    public ClientLoginResponse(Integer id, Date createDate, Date updateDate, @NotNull(message = "Mời bạn chọn trạng thái!") Integer status, String fullName, String numberPhone, LocalDate birthDay, String image, String email, String acount, String password, Integer gender, String addRess, String role) {
        super(id, createDate, updateDate, status);
        this.fullName = fullName;
        this.numberPhone = numberPhone;
        this.birthDay = birthDay;
        this.image = image;
        this.email = email;
        this.acount = acount;
        this.password = password;
        this.gender = gender;
        this.addRess = addRess;
        this.role = role;
    }
}

package com.example.shopgiayonepoly.dto.response;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import com.example.shopgiayonepoly.entites.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StaffResponse extends BaseDTO {
    private String nameImage;
    private String codeStaff;
    private String fullName;
    private String address;
    private Integer gender;
    private LocalDate birthDay;
    private String numberPhone;
    private String email;
    private Role role;


//    public StaffResponse(Integer id, Date createDate, Date updateDate, @NotNull(message = "Mời bạn chọn trạng thái!") Integer status, String nameImage, String codeStaff, String fullName, Integer gender, LocalDate birthDay, String numberPhone, String email, String nameRole) {
//        super(id, createDate, updateDate, status);
//        this.nameImage = nameImage;
//        this.codeStaff = codeStaff;
//        this.fullName = fullName;
//        this.gender = gender;
//        this.birthDay = birthDay;
//        this.numberPhone = numberPhone;
//        this.email = email;
//        this.nameRole = nameRole;
//    }


//    public StaffResponse(Integer id, Date createDate, Date updateDate, @NotNull(message = "Mời bạn chọn trạng thái!") Integer status, String nameImage, String codeStaff, String fullName, String address, Integer gender, LocalDate birthDay, String numberPhone, String email, String nameRole) {
//        super(id, createDate, updateDate, status);
//        this.nameImage = nameImage;
//        this.codeStaff = codeStaff;
//        this.fullName = fullName;
//        this.address = address;
//        this.gender = gender;
//        this.birthDay = birthDay;
//        this.numberPhone = numberPhone;
//        this.email = email;
//        this.nameRole = nameRole;
//    }


    public StaffResponse(Integer id, Date createDate, Date updateDate, @NotNull(message = "Mời bạn chọn trạng thái!") Integer status, String nameImage, String codeStaff, String fullName, String address, Integer gender, LocalDate birthDay, String numberPhone, String email, Role role) {
        super(id, createDate, updateDate, status);
        this.nameImage = nameImage;
        this.codeStaff = codeStaff;
        this.fullName = fullName;
        this.address = address;
        this.gender = gender;
        this.birthDay = birthDay;
        this.numberPhone = numberPhone;
        this.email = email;
        this.role = role;
    }
}

package com.example.shopgiayonepoly.dto.request;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import com.example.shopgiayonepoly.entites.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StaffRequest extends BaseDTO {
    @NotBlank(message = "")
    private String nameImage;
    @NotBlank(message = "Mã nhân viên không được để trống!")
    private String codeStaff;
    @NotBlank(message = "Tên nhân viên không được để trống!")
    private String fullName;
//    @NotBlank(message = "Dia chi nhân viên không được để trống!")
//    private String address;
    @NotNull(message = "Thanh pho không được để trống")
    private Integer city;
    @NotNull(message = "Huyen không được để trống")
    private Integer district;
    @NotNull(message = "Xa không được để trống")
    private Integer commune;
    @NotBlank(message = "Dia chi cu the không được để trống!")
    private String addRessDetail;
    @NotNull(message = "Giới tính không được để trống")
    private Integer gender;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDay;
    @NotBlank(message = "Số điện thoại không được để trống!")
    private String numberPhone;
    @NotNull(message = "Email không được để trống")
    private String email;
    @NotNull(message = "Tên chức vụ không được để trống")
    private Role role;
}

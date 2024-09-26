package com.example.shopgiayonepoly.dto.request;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
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
public class CustomerRequest extends BaseDTO {
    @NotBlank(message = "")
    private String nameImage;
    @NotBlank(message = "Tên khách hàng không được để trống!")
    private String fullName;
    @NotNull(message = "Giới tính không được để trống")
    private Integer gender;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDay;
    @NotBlank(message = "Số điện thoại không được để trống!")
    private String numberPhone;
    @NotNull(message = "Email không được để trống")
    private String email;
    @NotNull(message = "Địa chỉ không được để trống")
    private String addRessDetail;
}

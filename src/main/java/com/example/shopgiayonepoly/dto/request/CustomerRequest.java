package com.example.shopgiayonepoly.dto.request;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerRequest extends BaseDTO {
    private MultipartFile nameImage;
    private String imageString;
    private String fullName;
    @NotNull(message = "Vui lòng chọn giới tính!")
    private Integer gender;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDay;
    private String numberPhone;
    private String email;
    @NotBlank(message = "Vui lòng chọn thành phố!")
    private String province;
    @NotBlank(message = "Vui lòng chọn huyện!")
    private String district;
    @NotBlank(message = "Vui lòng chọn xã!")
    private String ward;
    private String addRessDetail;

    public CustomerRequest(Integer id, Date createDate, Date updateDate, @NotNull(message = "Mời bạn chọn trạng thái!") Integer status, @NotBlank(message = "") MultipartFile nameImage, @NotBlank(message = "Tên khách hàng không được để trống!") String fullName, @NotNull(message = "Giới tính không được để trống") Integer gender, LocalDate birthDay, @NotBlank(message = "Số điện thoại không được để trống!") String numberPhone, @NotBlank(message = "Email không được để trống") String email, @NotBlank(message = "Thanh pho không được để trống") String province, @NotBlank(message = "Huyen không được để trống") String district, @NotBlank(message = "Xa không được để trống") String ward, @NotNull(message = "Địa chỉ không được để trống") String addRessDetail) {
        super(id, createDate, updateDate, status);
        this.nameImage = nameImage;
        this.fullName = fullName;
        this.gender = gender;
        this.birthDay = birthDay;
        this.numberPhone = numberPhone;
        this.email = email;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.addRessDetail = addRessDetail;
    }
}

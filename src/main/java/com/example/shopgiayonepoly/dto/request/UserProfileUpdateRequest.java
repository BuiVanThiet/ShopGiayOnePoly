package com.example.shopgiayonepoly.dto.request;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateRequest extends BaseDTO {

    private MultipartFile nameImage; // Không validate MultipartFile

    private String imageString;
    private String account;

    @NotBlank(message = "Tên không được để trống")
    private String fullName;

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String numberPhone;

    @NotNull(message = "Giới tính không được để trống")
    private Integer gender;

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate birthDay;

    @NotBlank(message = "Tỉnh/Thành Phố không được để trống")
    private String province;

    @NotBlank(message = "Quận/Huyện không được để trống")
    private String district;

    @NotBlank(message = "Xã/Phường/Thị Trấn không được để trống")
    private String ward;

    @NotBlank(message = "Địa chỉ chi tiết không được để trống")
    private String addRessDetail;
}

package com.example.shopgiayonepoly.dto.request;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserProfileUpdateRequest extends BaseDTO {

    private MultipartFile nameImage;

    private String imageString;

    private String account;

    private String currentPassword; // Mật khẩu hiện tại
    private String newPassword;      // Mật khẩu mới
    private String confirmPassword;  // Xác nhận mật khẩu mới

    private String fullName;

    private String email;


    private String numberPhone;

    private Integer gender;

    private LocalDate birthDay;
    @NotBlank(message = "Vui lòng chọn thành phố!")
    private String province;

    @NotBlank(message = "Vui lòng chọn huyện!")
    private String district;

    @NotBlank(message = "Vui lòng chọn xã!")
    private String ward;

    private String addRessDetail;
}

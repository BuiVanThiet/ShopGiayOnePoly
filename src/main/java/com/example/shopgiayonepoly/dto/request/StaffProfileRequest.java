package com.example.shopgiayonepoly.dto.request;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StaffProfileRequest extends BaseDTO {
    private MultipartFile nameImage;

    private String imageStaffString;

    private String account;

    private String fullName;

    private String currentPassword;

    private String newPassword;

    private String confirmPassword;

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

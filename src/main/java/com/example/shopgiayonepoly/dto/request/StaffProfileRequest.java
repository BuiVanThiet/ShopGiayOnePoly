package com.example.shopgiayonepoly.dto.request;

import com.example.shopgiayonepoly.dto.base.BaseDTO;
import jakarta.validation.constraints.Email;
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

    private String password;

    @NotEmpty(message = "Email không được để trống")
    @Email(message = "Đây không phải là email")
    private String email;

    @Pattern(regexp = "^(0[1-9])+([0-9]{8})$", message = "Số điện thoại không hợp lệ")
    @NotEmpty(message = "Số điện thoại không được để trống")
    private String numberPhone;

    private Integer gender;

    private LocalDate birthDay;

    private String province;

    private String district;

    private String ward;

    private String addRessDetail;
}

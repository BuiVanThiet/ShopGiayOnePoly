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

    @NotBlank(message = "Tên đầy đủ không được để trống")
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "Số điện thoại không hợp lệ")
    private String numberPhone;

    @NotNull(message = "Giới tính không được để trống")
    private Integer gender; // Thay đổi từ int sang Integer để validate null

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate birthDay;

    @NotBlank(message = "Tỉnh không được để trống")
    private String province;

    @NotBlank(message = "Quận không được để trống")
    private String district;

    @NotBlank(message = "Phường không được để trống")
    private String ward;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String AddRess;

    private String addRessDetail; // Nếu không cần validate, có thể để không có chú thích
}

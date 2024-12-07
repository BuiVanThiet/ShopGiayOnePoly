package com.example.shopgiayonepoly.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String acount;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 1, max = 255, message = "Mật khẩu phải từ 1 đến 255 ký tự")
    private String password;

    @NotBlank(message = "Xác nhận mật khẩu không được trống")
    private String confirmPassword;
}

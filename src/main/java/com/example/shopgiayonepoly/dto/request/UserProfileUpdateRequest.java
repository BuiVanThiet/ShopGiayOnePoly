package com.example.shopgiayonepoly.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateRequest {
    private String fullName;
    private String email;
    private String numberPhone;
    private int gender;
    private LocalDate birthDay;
    private String province;
    private String district;
    private String ward;
}
